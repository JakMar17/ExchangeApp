package si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.assignments;

import java.time.LocalDateTime;

public class AssignmentBasicDTO {
    private Integer id;
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

    public AssignmentBasicDTO() {
    }

    public AssignmentBasicDTO(Integer id, String title, String classroomUrl, String description, LocalDateTime startDate, LocalDateTime endDate, Integer maxSubmissionsTotal, Integer maxSubmissionsPerStudent, Integer coinsPerSubmission, Integer coinsPrice, Integer noOfSubmissionsTotal, Integer noOfSubmissionsStudent, Boolean visible) {
        this.id = id;
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
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
