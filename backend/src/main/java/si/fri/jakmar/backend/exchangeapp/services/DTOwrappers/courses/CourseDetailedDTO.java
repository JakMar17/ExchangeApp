package si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.courses;

import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.assignments.AssignmentBasicDTO;
import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.users.UserDTO;

import java.util.List;

public class CourseDetailedDTO extends CourseDTO{
    private List<UserDTO> guardians;
    private List<UserDTO> studentsSignedIn;
    private List<UserDTO> studentsWhitelisted;
    private List<UserDTO> studentsBlacklisted;
    private String accessType;

    public CourseDetailedDTO(Integer courseId, String title, String description, String classroomUrl, UserDTO guardianMain, List<AssignmentBasicDTO> assignments, List<UserDTO> guardians, List<UserDTO> studentsSignedIn, List<UserDTO> studentsWhitelisted, List<UserDTO> studentsBlacklisted, String accessType) {
        super(courseId, title, description, classroomUrl, guardianMain, assignments);
        this.guardians = guardians;
        this.studentsSignedIn = studentsSignedIn;
        this.studentsWhitelisted = studentsWhitelisted;
        this.studentsBlacklisted = studentsBlacklisted;
        this.accessType = accessType;
    }

    public CourseDetailedDTO(Integer courseId, String title, String description, String classroomUrl, UserDTO guardianMain, List<AssignmentBasicDTO> assignments, Boolean userHasAdminRights, List<UserDTO> guardians, List<UserDTO> studentsSignedIn, List<UserDTO> studentsWhitelisted, List<UserDTO> studentsBlacklisted, String accessType) {
        super(courseId, title, description, classroomUrl, guardianMain, assignments, userHasAdminRights);
        this.guardians = guardians;
        this.studentsSignedIn = studentsSignedIn;
        this.studentsWhitelisted = studentsWhitelisted;
        this.studentsBlacklisted = studentsBlacklisted;
        this.accessType = accessType;
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
}
