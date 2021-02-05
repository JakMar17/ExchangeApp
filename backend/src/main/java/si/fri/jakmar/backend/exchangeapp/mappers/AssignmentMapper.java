package si.fri.jakmar.backend.exchangeapp.mappers;

import org.springframework.stereotype.Component;
import si.fri.jakmar.backend.exchangeapp.database.entities.assignments.AssignmentEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.assignments.AssignmentBasicDTO;

@Component
public class AssignmentMapper {

    public AssignmentBasicDTO castAssignmentEntityToAssignmentBasicDTO(AssignmentEntity assignmentEntity, UserEntity userEntity) {
        int noOfSubmissionsTotal = 0;
        int noOfSubmissionsStudent = 0;

        for(var submission : assignmentEntity.getSubmissions()) {
            if(submission.getAuthor().equals(userEntity))
                noOfSubmissionsStudent++;
            noOfSubmissionsTotal++;
        }

        return new AssignmentBasicDTO(
                assignmentEntity.getId(),
                assignmentEntity.getTitle(),
                assignmentEntity.getClassroomUrl(),
                assignmentEntity.getDescription(),
                assignmentEntity.getStartDate(),
                assignmentEntity.getEndDate(),
                assignmentEntity.getMaxSubmissionsTotal(),
                assignmentEntity.getMaxSubmissionsPerStudent(),
                assignmentEntity.getCoinsPerSubmission(),
                assignmentEntity.getCoinsPrice(),
                noOfSubmissionsTotal,
                noOfSubmissionsStudent,
                assignmentEntity.getVisible() == 1
        );
    }
}
