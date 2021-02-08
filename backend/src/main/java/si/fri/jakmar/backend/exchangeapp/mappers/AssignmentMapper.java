package si.fri.jakmar.backend.exchangeapp.mappers;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import si.fri.jakmar.backend.exchangeapp.database.entities.assignments.AssignmentEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.assignments.AssignmentDTO;

@Component
public class AssignmentMapper {

    public AssignmentDTO castAssignmentEntityToAssignmentBasicDTO(AssignmentEntity assignmentEntity, UserEntity userEntity) {
        int noOfSubmissionsTotal = assignmentEntity.getSubmissions().size();
        int noOfSubmissionsStudent = (int)
                CollectionUtils.emptyIfNull(assignmentEntity.getSubmissions())
                .stream()
                .filter(submission -> submission.getAuthor().equals(userEntity)).count();

        return AssignmentDTO.createBasicAssignmentDto(
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

    public AssignmentDTO castAssignmentEntityToAssignmentDTO(AssignmentEntity assignmentEntity, UserEntity userEntity) {
        int noOfSubmissionsTotal = assignmentEntity.getSubmissions().size();
        int noOfSubmissionsStudent = (int)
                CollectionUtils.emptyIfNull(assignmentEntity.getSubmissions())
                        .stream()
                        .filter(submission -> submission.getAuthor().equals(userEntity)).count();

        return AssignmentDTO.createFullAssignmentDto(
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
                assignmentEntity.getVisible() == 1,
                assignmentEntity.getInputDataType(),
                assignmentEntity.getOutputDataType(),
                assignmentEntity.getSubmissionCheckType().getDescription(),
                assignmentEntity.getSubmissionNotify() == 1,
                assignmentEntity.getPlagiarismWarning(),
                assignmentEntity.getPlagiarismLevel()
        );
    }
}
