package si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.courses;

import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.assignments.AssignmentBasicDTO;
import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.users.UserDTO;

import java.util.List;

public class CourseDTO extends CourseBasicDTO{
    private List<AssignmentBasicDTO> assignments;

    public CourseDTO(Integer courseId, String title, String description, String classroomUrl, UserDTO guardianMain, List<AssignmentBasicDTO> assignments) {
        super(courseId, title, description, classroomUrl, guardianMain);
        this.assignments = assignments;
    }

    public List<AssignmentBasicDTO> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<AssignmentBasicDTO> assignments) {
        this.assignments = assignments;
    }
}
