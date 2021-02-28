package si.fri.jakmar.backend.exchangeapp.api.courses.request_wrappers;

import java.util.List;

public class CourseSaveRequestWrapper {
    private String courseName;
    private String classroomUrl;
    private String description;
    private String accessLevel;
    private List<CourseSavePersonRequestWrapper> whitelistedStudents;
    private List<CourseSavePersonRequestWrapper> guardians;
    private Integer initialCoins;

    public CourseSaveRequestWrapper() {
    }

    public CourseSaveRequestWrapper(String courseName, String classroomUrl, String description, String accessLevel, List<CourseSavePersonRequestWrapper> whitelistedStudents, List<CourseSavePersonRequestWrapper> guardians, Integer initialCoins) {
        this.courseName = courseName;
        this.classroomUrl = classroomUrl;
        this.description = description;
        this.accessLevel = accessLevel;
        this.whitelistedStudents = whitelistedStudents;
        this.guardians = guardians;
        this.initialCoins = initialCoins;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getClassroomUrl() {
        return classroomUrl;
    }

    public void setClassroomUrl(String classroomUrl) {
        this.classroomUrl = classroomUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public List<CourseSavePersonRequestWrapper> getWhitelistedStudents() {
        return whitelistedStudents;
    }

    public void setWhitelistedStudents(List<CourseSavePersonRequestWrapper> whitelistedStudents) {
        this.whitelistedStudents = whitelistedStudents;
    }

    public List<CourseSavePersonRequestWrapper> getGuardians() {
        return guardians;
    }

    public void setGuardians(List<CourseSavePersonRequestWrapper> guardians) {
        this.guardians = guardians;
    }

    public Integer getInitialCoins() {
        return initialCoins;
    }

    public void setInitialCoins(Integer initialCoins) {
        this.initialCoins = initialCoins;
    }
}
