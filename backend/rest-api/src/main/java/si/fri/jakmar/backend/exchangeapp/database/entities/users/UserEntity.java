package si.fri.jakmar.backend.exchangeapp.database.entities.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import si.fri.jakmar.backend.exchangeapp.database.entities.assignments.AssignmentEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.courses.CourseEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.purchases.PurchaseEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.submissions.SubmissionEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "user")
public class UserEntity implements UserDetails {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email")
    private String username;
    private String name;
    private String surname;
    private String password;
    @Column(name = "personal_number")
    private String personalNumber;
    @Column(name = "user_created")
    private LocalDateTime userCreated = LocalDateTime.now(ZoneOffset.UTC);
    @Column(name = "user_deleted")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean deleted = false;
    @Column(name = "confirmation_string")
    private String confirmationString;

    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    private UserType userType = UserType.OTHER;

    @OneToMany(mappedBy = "guardianMain")
    @Where(clause = "course_deleted = false")
    private List<CourseEntity> createdCourses;

    @ManyToMany(mappedBy = "usersSignedInCourse", cascade = CascadeType.ALL)
    @Where(clause = "course_deleted = false")
    private List<CourseEntity> usersCourses;

    @ManyToMany(mappedBy = "usersBlacklisted")
    @Where(clause = "course_deleted = false")
    private List<CourseEntity> blacklistedCourses;

    @ManyToMany(mappedBy = "usersWhitelisted")
    @Where(clause = "course_deleted = false")
    private List<CourseEntity> whitelistedCourses;

    @ManyToMany(mappedBy = "usersGuardians")
    @Where(clause = "course_deleted = false")
    private List<CourseEntity> guardiansCourses;

    @OneToMany(mappedBy = "author")
    @Where(clause = "assignment_deleted = false")
    private List<AssignmentEntity> createdAssignments;

    @OneToMany(mappedBy = "author")
    @Where(clause = "submission_deleted = false")
    private List<SubmissionEntity> submissions;

    @OneToMany(mappedBy = "userBuying")
    private List<PurchaseEntity> purchases;

    @Column(name = "registration_status")
    @Enumerated(EnumType.STRING)
    private UserRegistrationStatus registrationStatus = UserRegistrationStatus.PENDING_CONFIRMATION;

    public UserEntity() {
    }

    public UserEntity(String username, String name, String surname, String password, String personalNumber, UserRegistrationStatus registrationStatus, UserType userType) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.personalNumber = personalNumber;
        this.registrationStatus = registrationStatus;
        this.userType = userType;
    }

    public UserEntity(String username, String name, String surname, String password, String personalNumber, String confirmationString, UserType userType, UserRegistrationStatus registrationStatus) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.personalNumber = personalNumber;
        this.confirmationString = confirmationString;
        this.userType = userType;
        this.registrationStatus = registrationStatus;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isEnabled();
    }

    @Override
    public boolean isEnabled() {
        return registrationStatus == UserRegistrationStatus.REGISTERED && !deleted;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userType.name()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
