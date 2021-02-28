package si.fri.jakmar.backend.exchangeapp.database.entities.courses;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import si.fri.jakmar.backend.exchangeapp.database.entities.assignments.AssignmentEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.notifications.NotificationEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "course")
public class CourseEntity {

    @Id
    @Column(name = "course_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;
    @Column(name = "course_title")
    private String courseTitle;
    @Column(name = "course_description")
    private String courseDescription;
    @Column(name = "course_classroom_url")
    private String courseClassroomUrl;
    @Column(name = "initial_coins")
    private Integer initialCoins = 0;
    @Column(name = "course_created")
    private LocalDateTime courseCreated = LocalDateTime.now(ZoneOffset.UTC);
    @Column(name = "course_archived")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean archived = false;
    @Column(name = "course_deleted")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "access_level_id", referencedColumnName = "access_level_id")
    private CourseAccessLevelEntity accessLevel;

    @OneToOne
    @JoinColumn(name = "access_password_id", referencedColumnName = "access_password_id")
    private CourseAccessPassword accessPassword;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserEntity guardianMain;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "STUDENT_SIGNED_IN", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<UserEntity> usersSignedInCourse;

    @ManyToMany
    @JoinTable(name = "STUDENT_BLACKLIST", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<UserEntity> usersBlacklisted;

    @ManyToMany
    @JoinTable(name = "STUDENT_WHITELIST", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<UserEntity> usersWhitelisted;

    @ManyToMany
    @JoinTable(name = "GUARDIAN", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<UserEntity> usersGuardians;

    @OneToMany(mappedBy = "course")
    @Where(clause = "assignment_deleted = false")
    private List<AssignmentEntity> assignments;

    @OneToMany(mappedBy = "course")
    @Where(clause = "notification_deleted = false")
    private List<NotificationEntity> notifications;

    public CourseEntity() {
    }

    public CourseEntity(Integer courseId, String courseTitle, String courseDescription, String courseClassroomUrl, Integer initialCoins, LocalDateTime courseCreated, Boolean archived, CourseAccessLevelEntity accessLevel, CourseAccessPassword accessPassword, UserEntity guardianMain, List<UserEntity> usersSignedInCourse, List<UserEntity> usersBlacklisted, List<UserEntity> usersWhitelisted, List<UserEntity> usersGuardians, List<AssignmentEntity> assignments) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.courseClassroomUrl = courseClassroomUrl;
        this.initialCoins = initialCoins;
        this.courseCreated = courseCreated;
        this.archived = archived;
        this.accessLevel = accessLevel;
        this.accessPassword = accessPassword;
        this.guardianMain = guardianMain;
        this.usersSignedInCourse = usersSignedInCourse;
        this.usersBlacklisted = usersBlacklisted;
        this.usersWhitelisted = usersWhitelisted;
        this.usersGuardians = usersGuardians;
        this.assignments = assignments;
    }

    public CourseEntity courseUpdater(String courseTitle, String courseDescription, String courseClassroomUrl, Integer initialCoins, CourseAccessLevelEntity accessLevel, CourseAccessPassword accessPassword, List<UserEntity> usersSignedInCourse, List<UserEntity> usersBlacklisted, List<UserEntity> usersWhitelisted, List<UserEntity> usersGuardians, Boolean archived) {
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.courseClassroomUrl = courseClassroomUrl;
        this.initialCoins = initialCoins;
        this.accessLevel = accessLevel;
        this.accessPassword = accessPassword;
        this.usersSignedInCourse = usersSignedInCourse;
        this.usersBlacklisted = usersBlacklisted;
        this.usersWhitelisted = usersWhitelisted;
        this.usersGuardians = usersGuardians;
        this.archived = archived;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseEntity that = (CourseEntity) o;
        return Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseClassroomUrl() {
        return courseClassroomUrl;
    }

    public void setCourseClassroomUrl(String courseClassroomUrl) {
        this.courseClassroomUrl = courseClassroomUrl;
    }

    public Integer getInitialCoins() {
        return initialCoins;
    }

    public void setInitialCoins(Integer initialCoins) {
        this.initialCoins = initialCoins;
    }

    public LocalDateTime getCourseCreated() {
        return courseCreated;
    }

    public void setCourseCreated(LocalDateTime courseCreated) {
        this.courseCreated = courseCreated;
    }

    public CourseAccessLevelEntity getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(CourseAccessLevelEntity accessLevel) {
        this.accessLevel = accessLevel;
    }

    public CourseAccessPassword getAccessPassword() {
        return accessPassword;
    }

    public void setAccessPassword(CourseAccessPassword accessPassword) {
        this.accessPassword = accessPassword;
    }

    public UserEntity getGuardianMain() {
        return guardianMain;
    }

    public void setGuardianMain(UserEntity guardianMain) {
        this.guardianMain = guardianMain;
    }

    public List<UserEntity> getUsersSignedInCourse() {
        return usersSignedInCourse;
    }

    public void setUsersSignedInCourse(List<UserEntity> usersSignedInCourse) {
        this.usersSignedInCourse = usersSignedInCourse;
    }

    public List<UserEntity> getUsersBlacklisted() {
        return usersBlacklisted;
    }

    public void setUsersBlacklisted(List<UserEntity> usersBlacklisted) {
        this.usersBlacklisted = usersBlacklisted;
    }

    public List<UserEntity> getUsersWhitelisted() {
        return usersWhitelisted;
    }

    public void setUsersWhitelisted(List<UserEntity> usersWhiteListed) {
        this.usersWhitelisted = usersWhiteListed;
    }

    public List<UserEntity> getUsersGuardians() {
        return usersGuardians;
    }

    public void setUsersGuardians(List<UserEntity> usersGuardians) {
        this.usersGuardians = usersGuardians;
    }

    public List<AssignmentEntity> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<AssignmentEntity> assignments) {
        this.assignments = assignments;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public List<NotificationEntity> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationEntity> notifications) {
        this.notifications = notifications;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
