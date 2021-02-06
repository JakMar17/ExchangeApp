package si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.courses;

import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.assignments.AssignmentBasicDTO;
import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.users.UserDTO;

import java.util.List;

public class CourseDTO extends CourseBasicDTO{
    private List<AssignmentBasicDTO> assignments;
    private Boolean userHasAdminRights;

    public CourseDTO(Integer courseId, String title, String description, String classroomUrl, UserDTO guardianMain, List<AssignmentBasicDTO> assignments) {
        super(courseId, title, description, classroomUrl, guardianMain);
        this.assignments = assignments;
    }

    public CourseDTO(Integer courseId, String title, String description, String classroomUrl, UserDTO guardianMain, List<AssignmentBasicDTO> assignments, Boolean userHasAdminRights) {
        super(courseId, title, description, classroomUrl, guardianMain);
        this.assignments = assignments;
        this.userHasAdminRights = userHasAdminRights;
    }



    public List<AssignmentBasicDTO> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<AssignmentBasicDTO> assignments) {
        this.assignments = assignments;
    }

    public Boolean getUserHasAdminRights() {
        return userHasAdminRights;
    }

    public void setUserHasAdminRights(Boolean userHasAdminRights) {
        this.userHasAdminRights = userHasAdminRights;
    }
}
