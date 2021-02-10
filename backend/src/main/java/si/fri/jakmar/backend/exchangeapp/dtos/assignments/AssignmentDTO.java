package si.fri.jakmar.backend.exchangeapp.dtos.assignments;

import org.apache.commons.collections4.CollectionUtils;
import si.fri.jakmar.backend.exchangeapp.database.entities.assignments.AssignmentEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;

import java.time.LocalDateTime;

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

    private String testType;
    private Boolean notifyOnEmail;
    private Integer plagiarismWarning;
    private Integer plagiarismLevel;

    public AssignmentDTO() {
    }

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

    private AssignmentDTO(Integer assignmentId, String title, String classroomUrl, String description, LocalDateTime startDate, LocalDateTime endDate, Integer maxSubmissionsTotal, Integer maxSubmissionsPerStudent, Integer coinsPerSubmission, Integer coinsPrice, Integer noOfSubmissionsTotal, Integer noOfSubmissionsStudent, Boolean visible, Boolean archived, String inputExtension, String outputExtension, String testType, Boolean notifyOnEmail, Integer plagiarismWarning, Integer plagiarismLevel) {
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
                entity.getVisible() == 1,
                entity.getArchived()
        );
    }

    public static AssignmentDTO castFullFromEntity(AssignmentEntity entity, Integer noOfSubmissionsStudent) {
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
                entity.getVisible() == 1,
                entity.getArchived(),
                entity.getInputDataType(),
                entity.getOutputDataType(),
                entity.getSubmissionCheckType().getDescription(),
                entity.getSubmissionNotify() == 1,
                entity.getPlagiarismWarning(),
                entity.getPlagiarismLevel()
        );
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Integer getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Integer assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getMaxSubmissionsTotal() {
        return maxSubmissionsTotal;
    }

    public void setMaxSubmissionsTotal(Integer maxSubmissionsTotal) {
        this.maxSubmissionsTotal = maxSubmissionsTotal;
    }

    public Integer getMaxSubmissionsPerStudent() {
        return maxSubmissionsPerStudent;
    }

    public void setMaxSubmissionsPerStudent(Integer maxSubmissionsPerStudent) {
        this.maxSubmissionsPerStudent = maxSubmissionsPerStudent;
    }

    public Integer getCoinsPerSubmission() {
        return coinsPerSubmission;
    }

    public void setCoinsPerSubmission(Integer coinsPerSubmission) {
        this.coinsPerSubmission = coinsPerSubmission;
    }

    public Integer getCoinsPrice() {
        return coinsPrice;
    }

    public void setCoinsPrice(Integer coinsPrice) {
        this.coinsPrice = coinsPrice;
    }

    public Integer getNoOfSubmissionsTotal() {
        return noOfSubmissionsTotal;
    }

    public void setNoOfSubmissionsTotal(Integer noOfSubmissionsTotal) {
        this.noOfSubmissionsTotal = noOfSubmissionsTotal;
    }

    public Integer getNoOfSubmissionsStudent() {
        return noOfSubmissionsStudent;
    }

    public void setNoOfSubmissionsStudent(Integer noOfSubmissionsStudent) {
        this.noOfSubmissionsStudent = noOfSubmissionsStudent;
    }

    public String getInputExtension() {
        return inputExtension;
    }

    public void setInputExtension(String inputExtension) {
        this.inputExtension = inputExtension;
    }

    public String getOutputExtension() {
        return outputExtension;
    }

    public void setOutputExtension(String outputExtension) {
        this.outputExtension = outputExtension;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public Boolean getNotifyOnEmail() {
        return notifyOnEmail;
    }

    public void setNotifyOnEmail(Boolean notifyOnEmail) {
        this.notifyOnEmail = notifyOnEmail;
    }

    public Integer getPlagiarismWarning() {
        return plagiarismWarning;
    }

    public void setPlagiarismWarning(Integer plagiarismWarning) {
        this.plagiarismWarning = plagiarismWarning;
    }

    public Integer getPlagiarismLevel() {
        return plagiarismLevel;
    }

    public void setPlagiarismLevel(Integer plagiarismLevel) {
        this.plagiarismLevel = plagiarismLevel;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }
}
