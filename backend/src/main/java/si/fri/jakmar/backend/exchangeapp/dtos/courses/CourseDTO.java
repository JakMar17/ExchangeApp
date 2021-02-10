package si.fri.jakmar.backend.exchangeapp.dtos.courses;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.catalina.User;
import org.apache.commons.collections4.CollectionUtils;
import si.fri.jakmar.backend.exchangeapp.database.entities.courses.CourseEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.dtos.assignments.AssignmentDTO;
import si.fri.jakmar.backend.exchangeapp.dtos.users.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDTO{
    //basic info
    private Integer courseId;
    private String title;
    private String description;
    private String classroomURL;
    private UserDTO guardianMain;
    private String accessLevel;

    private List<AssignmentDTO> assignments;
    private Boolean userCanEditCourse;
    private Integer usersCoins;

    // all info
    private List<UserDTO> guardians;
    private List<UserDTO> studentsSignedIn;
    private List<UserDTO> studentsWhitelisted;
    private List<UserDTO> studentsBlacklisted;
    private String accessType;
    private Integer initialCoins;

    private String accessPassword;

    public CourseDTO() {
    }

    private CourseDTO(Integer courseId, String title, String description, String classroomURL, UserDTO guardianMain, String accessLevel) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.classroomURL = classroomURL;
        this.guardianMain = guardianMain;
        this.accessLevel = accessLevel;
    }

    private CourseDTO(Integer courseId, String title, String description, String classroomURL, UserDTO guardianMain, String accessLevel, List<AssignmentDTO> assignments, Integer usersCoins) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.classroomURL = classroomURL;
        this.guardianMain = guardianMain;
        this.accessLevel = accessLevel;
        this.assignments = assignments;
        this.usersCoins = usersCoins;
    }

    public CourseDTO(Integer courseId, String title, String description, String classroomURL, UserDTO guardianMain, String accessLevel, List<AssignmentDTO> assignments, Boolean userCanEditCourse, List<UserDTO> guardians, List<UserDTO> studentsSignedIn, List<UserDTO> studentsWhitelisted, List<UserDTO> studentsBlacklisted, String accessType, Integer initialCoins, String accessPassword, Integer usersCoins) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.classroomURL = classroomURL;
        this.guardianMain = guardianMain;
        this.accessLevel = accessLevel;
        this.assignments = assignments;
        this.userCanEditCourse = userCanEditCourse;
        this.guardians = guardians;
        this.studentsSignedIn = studentsSignedIn;
        this.studentsWhitelisted = studentsWhitelisted;
        this.studentsBlacklisted = studentsBlacklisted;
        this.accessType = accessType;
        this.initialCoins = initialCoins;
        this.accessPassword = accessPassword;
        this.usersCoins = usersCoins;
    }

    public static CourseDTO castBasicFromEntity (CourseEntity entity) {
        return new CourseDTO(
                entity.getCourseId(),
                entity.getCourseTitle(),
                entity.getCourseDescription(),
                entity.getCourseClassroomUrl(),
                UserDTO.castFromEntity(entity.getGuardianMain(), false, false),
                entity.getAccessLevel().getDescription()
        );
    }

    public static CourseDTO castFromEntity(CourseEntity entity, UserEntity user, Integer usersCoins) {
        return new CourseDTO(
                entity.getCourseId(),
                entity.getCourseTitle(),
                entity.getCourseDescription(),
                entity.getCourseClassroomUrl(),
                UserDTO.castFromEntity(entity.getGuardianMain(), false, false),
                entity.getAccessLevel().getDescription(),
                CollectionUtils.emptyIfNull(entity.getAssignments()).stream()
                    .map(e -> AssignmentDTO.castBasicFromEntity(e, user))
                    .collect(Collectors.toList()),
                usersCoins
        );
    }

    public static CourseDTO castFullFromEntity(CourseEntity entity, UserEntity user, boolean userCanEditCourse, Integer usersCoins) {
        return new CourseDTO(
                entity.getCourseId(),
                entity.getCourseTitle(),
                entity.getCourseDescription(),
                entity.getCourseClassroomUrl(),
                UserDTO.castFromEntity(entity.getGuardianMain(), false, false),
                entity.getAccessLevel().getDescription(),
                CollectionUtils.emptyIfNull(entity.getAssignments()).stream()
                    .map(e -> AssignmentDTO.castBasicFromEntity(e, user))
                    .collect(Collectors.toList()),
                userCanEditCourse,
                CollectionUtils.emptyIfNull(entity.getUsersGuardians()).stream()
                    .map(e -> UserDTO.castFromEntity(e, false, false))
                    .collect(Collectors.toList()),
                CollectionUtils.emptyIfNull(entity.getUsersSignedInCourse()).stream()
                    .map(e -> UserDTO.castFromEntity(e, false, false))
                    .collect(Collectors.toList()),
                CollectionUtils.emptyIfNull(entity.getUsersWhitelisted()).stream()
                    .map(e -> UserDTO.castFromEntity(e, false, false))
                    .collect(Collectors.toList()),
                CollectionUtils.emptyIfNull(entity.getUsersBlacklisted()).stream()
                    .map(e -> UserDTO.castFromEntity(e, false, false))
                    .collect(Collectors.toList()),
                entity.getAccessLevel().getDescription(),
                entity.getInitialCoins(),
                entity.getAccessPassword() == null
                    ? null
                    : entity.getAccessPassword().getPassword(),
                usersCoins
        );
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClassroomURL() {
        return classroomURL;
    }

    public void setClassroomURL(String classroomURL) {
        this.classroomURL = classroomURL;
    }

    public UserDTO getGuardianMain() {
        return guardianMain;
    }

    public void setGuardianMain(UserDTO guardianMain) {
        this.guardianMain = guardianMain;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public List<AssignmentDTO> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<AssignmentDTO> assignments) {
        this.assignments = assignments;
    }

    public Boolean getUserCanEditCourse() {
        return userCanEditCourse;
    }

    public void setUserCanEditCourse(Boolean userCanEditCourse) {
        this.userCanEditCourse = userCanEditCourse;
    }

    public List<UserDTO> getGuardians() {
        return guardians;
    }

    public void setGuardians(List<UserDTO> guardians) {
        this.guardians = guardians;
    }

    public List<UserDTO> getStudentsSignedIn() {
        return studentsSignedIn;
    }

    public void setStudentsSignedIn(List<UserDTO> studentsSignedIn) {
        this.studentsSignedIn = studentsSignedIn;
    }

    public List<UserDTO> getStudentsWhitelisted() {
        return studentsWhitelisted;
    }

    public void setStudentsWhitelisted(List<UserDTO> studentsWhitelisted) {
        this.studentsWhitelisted = studentsWhitelisted;
    }

    public List<UserDTO> getStudentsBlacklisted() {
        return studentsBlacklisted;
    }

    public void setStudentsBlacklisted(List<UserDTO> studentsBlacklisted) {
        this.studentsBlacklisted = studentsBlacklisted;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public Integer getInitialCoins() {
        return initialCoins;
    }

    public void setInitialCoins(Integer initialCoins) {
        this.initialCoins = initialCoins;
    }

    public String getAccessPassword() {
        return accessPassword;
    }

    public void setAccessPassword(String accessPassword) {
        this.accessPassword = accessPassword;
    }

    public Integer getUsersCoins() {
        return usersCoins;
    }

    public void setUsersCoins(Integer usersCoins) {
        this.usersCoins = usersCoins;
    }
}
