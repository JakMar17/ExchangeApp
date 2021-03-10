package si.fri.jakmar.backend.exchangeapp.dtos.assignments;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import si.fri.jakmar.backend.exchangeapp.database.entities.assignments.AssignmentEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.assignments.SubmissionCheck;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.dtos.submissions.SubmissionDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class AssignmentDTO {
    private Integer assignmentId;
    private String title;
    private String classroomUrl;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer maxSubmissionsTotal;
    private Integer maxSubmissionsPerStudent;
    private Integer coinsPerSubmission;
    private Integer coinsPrice;
    private Integer noOfSubmissionsTotal;
    private Integer noOfSubmissionsStudent;
    private Boolean visible;
    private Boolean archived;

    private String inputExtension;
    private String outputExtension;

    private SubmissionCheck testType;
    private Boolean notifyOnEmail;
    private Integer plagiarismWarning;
    private Integer plagiarismLevel;

    private List<SubmissionDTO> mySubmissions;
    private List<SubmissionDTO> boughtSubmissions;

    private Integer sourceId;
    private String sourceName;
    private String sourceLanguage;
    private Integer sourceTimeout;

    private AssignmentDTO(Integer assignmentId, String title, String classroomUrl, String description, LocalDateTime startDate, LocalDateTime endDate, Integer maxSubmissionsTotal, Integer maxSubmissionsPerStudent, Integer coinsPerSubmission, Integer coinsPrice, Integer noOfSubmissionsTotal, Integer noOfSubmissionsStudent, Boolean visible, Boolean archived) {
        this.assignmentId = assignmentId;
        this.title = title;
        this.classroomUrl = classroomUrl;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxSubmissionsTotal = maxSubmissionsTotal;
        this.maxSubmissionsPerStudent = maxSubmissionsPerStudent;
        this.coinsPerSubmission = coinsPerSubmission;
        this.coinsPrice = coinsPrice;
        this.noOfSubmissionsTotal = noOfSubmissionsTotal;
        this.noOfSubmissionsStudent = noOfSubmissionsStudent;
        this.visible = visible;
        this.archived = archived;
    }

    private AssignmentDTO(Integer assignmentId, String title, String classroomUrl, String description, LocalDateTime startDate, LocalDateTime endDate, Integer maxSubmissionsTotal, Integer maxSubmissionsPerStudent, Integer coinsPerSubmission, Integer coinsPrice, Integer noOfSubmissionsTotal, Integer noOfSubmissionsStudent, Boolean visible, Boolean archived, String inputExtension, String outputExtension, SubmissionCheck testType, Boolean notifyOnEmail, Integer plagiarismWarning, Integer plagiarismLevel, Integer sourceId, String sourceName, String sourceLanguage, Integer sourceTimeout) {
        this.assignmentId = assignmentId;
        this.title = title;
        this.classroomUrl = classroomUrl;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxSubmissionsTotal = maxSubmissionsTotal;
        this.maxSubmissionsPerStudent = maxSubmissionsPerStudent;
        this.coinsPerSubmission = coinsPerSubmission;
        this.coinsPrice = coinsPrice;
        this.noOfSubmissionsTotal = noOfSubmissionsTotal;
        this.noOfSubmissionsStudent = noOfSubmissionsStudent;
        this.visible = visible;
        this.archived = archived;
        this.inputExtension = inputExtension;
        this.outputExtension = outputExtension;
        this.testType = testType;
        this.notifyOnEmail = notifyOnEmail;
        this.plagiarismWarning = plagiarismWarning;
        this.plagiarismLevel = plagiarismLevel;
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.sourceLanguage = sourceLanguage;
        this.sourceTimeout = sourceTimeout;
    }

    private AssignmentDTO(Integer assignmentId, String title, String classroomUrl, String description, LocalDateTime startDate, LocalDateTime endDate, Integer maxSubmissionsTotal, Integer maxSubmissionsPerStudent, Integer coinsPerSubmission, Integer coinsPrice, Integer noOfSubmissionsTotal, Integer noOfSubmissionsStudent, Boolean visible, Boolean archived, String inputExtension, String outputExtension, SubmissionCheck testType, Boolean notifyOnEmail, Integer plagiarismWarning, Integer plagiarismLevel, List<SubmissionDTO> mySubmissions, List<SubmissionDTO> boughtSubmissions) {
        this.assignmentId = assignmentId;
        this.title = title;
        this.classroomUrl = classroomUrl;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxSubmissionsTotal = maxSubmissionsTotal;
        this.maxSubmissionsPerStudent = maxSubmissionsPerStudent;
        this.coinsPerSubmission = coinsPerSubmission;
        this.coinsPrice = coinsPrice;
        this.noOfSubmissionsTotal = noOfSubmissionsTotal;
        this.noOfSubmissionsStudent = noOfSubmissionsStudent;
        this.visible = visible;
        this.archived = archived;
        this.inputExtension = inputExtension;
        this.outputExtension = outputExtension;
        this.testType = testType;
        this.notifyOnEmail = notifyOnEmail;
        this.plagiarismWarning = plagiarismWarning;
        this.plagiarismLevel = plagiarismLevel;
        this.mySubmissions = mySubmissions;
        this.boughtSubmissions = boughtSubmissions;
    }

    public static AssignmentDTO castBasicFromEntity(AssignmentEntity entity, UserEntity user) {
        int noOfSubmissionsStudent = (int) CollectionUtils.emptyIfNull(entity.getSubmissions()).stream()
                .filter(e -> e.getAuthor().equals(user))
                .count();

        return new AssignmentDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getClassroomUrl(),
                entity.getDescription(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getMaxSubmissionsTotal(),
                entity.getMaxSubmissionsPerStudent(),
                entity.getCoinsPerSubmission(),
                entity.getCoinsPrice(),
                entity.getSubmissions() != null
                    ? entity.getSubmissions().size()
                    : 0,
                noOfSubmissionsStudent,
                entity.getVisible(),
                entity.getArchived()
        );
    }

    public static AssignmentDTO castFullFromEntity(AssignmentEntity entity, Integer noOfSubmissionsStudent) {
        var source = entity.getSource();

        return new AssignmentDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getClassroomUrl(),
                entity.getDescription(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getMaxSubmissionsTotal(),
                entity.getMaxSubmissionsPerStudent(),
                entity.getCoinsPerSubmission(),
                entity.getCoinsPrice(),
                entity.getSubmissions() != null
                        ? entity.getSubmissions().size()
                        : 0,
                noOfSubmissionsStudent,
                entity.getVisible(),
                entity.getArchived(),
                entity.getInputDataType(),
                entity.getOutputDataType(),
                entity.getSubmissionCheck(),
                entity.getSubmissionNotify(),
                entity.getPlagiarismWarning(),
                entity.getPlagiarismLevel(),
                source != null ? source.getId() : null,
                source != null ? source.getProgramName() : null,
                source != null ? source.getProgramLanguage() : null,
                source != null ? source.getTimeout() : null
        );
    }

    public static AssignmentDTO castFromEntityWithSubmissions(AssignmentEntity entity, List<SubmissionDTO> usersSubmissions, List<SubmissionDTO> boughtSubmissions) {
        return new AssignmentDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getClassroomUrl(),
                entity.getDescription(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getMaxSubmissionsTotal(),
                entity.getMaxSubmissionsPerStudent(),
                entity.getCoinsPerSubmission(),
                entity.getCoinsPrice(),
                entity.getSubmissions() != null
                        ? entity.getSubmissions().size()
                        : 0,
                usersSubmissions != null
                    ? usersSubmissions.size()
                    : 0,
                entity.getVisible(),
                entity.getArchived(),
                entity.getInputDataType(),
                entity.getOutputDataType(),
                entity.getSubmissionCheck(),
                entity.getSubmissionNotify(),
                entity.getPlagiarismLevel(),
                entity.getPlagiarismLevel(),
                usersSubmissions,
                boughtSubmissions
        );
    }
}
