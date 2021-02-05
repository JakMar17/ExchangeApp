package si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.courses;

import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.users.UserDTO;

public class CourseBasicDTO {
    private Integer courseId;
    private String title;
    private String description;
    private String classroomUrl;
    private UserDTO guardianMain;

    public CourseBasicDTO() {
    }

    public CourseBasicDTO(Integer courseId, String title, String description, String classroomUrl, UserDTO guardianMain) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.classroomUrl = classroomUrl;
        this.guardianMain = guardianMain;
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

    public String getClassroomUrl() {
        return classroomUrl;
    }

    public void setClassroomUrl(String classroomUrl) {
        this.classroomUrl = classroomUrl;
    }

    public UserDTO getGuardianMain() {
        return guardianMain;
    }

    public void setGuardianMain(UserDTO guardianMain) {
        this.guardianMain = guardianMain;
    }
}
