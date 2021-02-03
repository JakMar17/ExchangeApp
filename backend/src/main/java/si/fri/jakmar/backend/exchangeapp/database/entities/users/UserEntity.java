package si.fri.jakmar.backend.exchangeapp.database.entities.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import si.fri.jakmar.backend.exchangeapp.database.entities.assignments.AssignmentEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.courses.CourseEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.purchases.PurchaseEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.submissions.SubmissionEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;
    private String name;
    private String surname;
    private String password;
    @Column(name = "personal_number")
    private String personalNumber;
    @Column(name = "user_created")
    private Timestamp userCreated = new Timestamp(System.currentTimeMillis());

    @ManyToOne
    @JoinColumn(name = "user_type_id")
    private UserTypeEntity userType;

    @JsonIgnoreProperties({"guardianMain", "usersSignedInCourse"})
    @OneToMany(mappedBy = "guardianMain")
    private List<CourseEntity> createdCourses;

    @JsonIgnoreProperties({"guardianMain", "usersSignedInCourse"})
    @ManyToMany(mappedBy = "usersSignedInCourse")
    private List<CourseEntity> usersCourses;

    @JsonIgnoreProperties({"guardianMain", "usersSignedInCourse"})
    @ManyToMany(mappedBy = "usersBlacklisted")
    private List<CourseEntity> blacklistedCourses;

    @JsonIgnoreProperties({"guardianMain", "usersSignedInCourse"})
    @ManyToMany(mappedBy = "usersWhitelisted")
    private List<CourseEntity> whitelistedCourses;

    @JsonIgnoreProperties({"guardianMain", "usersSignedInCourse"})
    @ManyToMany(mappedBy = "usersGuardians")
    private List<CourseEntity> guardinasCourses;

    @JsonIgnoreProperties({"author"})
    @OneToMany(mappedBy = "author")
    private List<AssignmentEntity> createdAssignemnts;

    @JsonIgnoreProperties({"author"})
    @OneToMany(mappedBy = "author")
    private List<SubmissionEntity> submissions;

    @JsonIgnoreProperties({"userBuying"})
    @OneToMany(mappedBy = "userBuying")
    private List<PurchaseEntity> purchases;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public Timestamp getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(Timestamp userCreated) {
        this.userCreated = userCreated;
    }

    public UserTypeEntity getUserType() {
        return userType;
    }

    public void setUserType(UserTypeEntity userType) {
        this.userType = userType;
    }

    public List<CourseEntity> getCreatedCourses() {
        return createdCourses;
    }

    public void setCreatedCourses(List<CourseEntity> createdCourses) {
        this.createdCourses = createdCourses;
    }

    public List<CourseEntity> getUsersCourses() {
        return usersCourses;
    }

    public void setUsersCourses(List<CourseEntity> usersCourses) {
        this.usersCourses = usersCourses;
    }

    public List<CourseEntity> getBlacklistedCourses() {
        return blacklistedCourses;
    }

    public void setBlacklistedCourses(List<CourseEntity> blacklistedCourses) {
        this.blacklistedCourses = blacklistedCourses;
    }

    public List<CourseEntity> getWhitelistedCourses() {
        return whitelistedCourses;
    }

    public void setWhitelistedCourses(List<CourseEntity> whitelistedCourses) {
        this.whitelistedCourses = whitelistedCourses;
    }

    public List<CourseEntity> getGuardinasCourses() {
        return guardinasCourses;
    }

    public void setGuardinasCourses(List<CourseEntity> guardinasCourses) {
        this.guardinasCourses = guardinasCourses;
    }

    public List<AssignmentEntity> getCreatedAssignemnts() {
        return createdAssignemnts;
    }

    public void setCreatedAssignemnts(List<AssignmentEntity> createdAssignemnts) {
        this.createdAssignemnts = createdAssignemnts;
    }

    public List<SubmissionEntity> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<SubmissionEntity> submissions) {
        this.submissions = submissions;
    }

    public List<PurchaseEntity> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<PurchaseEntity> purchases) {
        this.purchases = purchases;
    }
}