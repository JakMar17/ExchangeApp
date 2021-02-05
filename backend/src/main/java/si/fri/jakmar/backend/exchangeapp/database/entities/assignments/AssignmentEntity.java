package si.fri.jakmar.backend.exchangeapp.database.entities.assignments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import si.fri.jakmar.backend.exchangeapp.database.entities.courses.CourseEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.submissions.SubmissionEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Entity
@Table(name = "assignment")
public class AssignmentEntity {

    @Id
    @Column(name = "assignment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "assignment_title")
    private String title;
    @Column(name = "assignment_classroom_url")
    private String classroomUrl;
    @Column(name = "assignment_description")
    private String description;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @Column(name = "max_submissions_total")
    private Integer maxSubmissionsTotal;
    @Column(name = "max_submissions_student")
    private Integer maxSubmissionsPerStudent;
    @Column(name = "coins_per_submission")
    private Integer coinsPerSubmission;
    @Column(name = "coins_price")
    private Integer coinsPrice;
    @Column(name = "input_data_type")
    private String inputDataType;
    @Column(name = "output_data_type")
    private Integer outputDataType;
    @Column(name = "submission_notify")
    private Integer submissionNotify;
    @Column(name = "plagiarism_warning")
    private Integer plagiarismWarning;
    @Column(name = "plagiarism_level")
    private Integer plagiarismLevel;
    @Column(name = "visible")
    private Integer visible;
    @Column(name = "assignment_date_created")
    private LocalDateTime created = LocalDateTime.now(ZoneOffset.UTC);

    @ManyToOne
    @JoinColumn(name = "submission_check_id")
    private SubmissionCheckEntity submissionCheckType;

    @ManyToOne
    @JoinColumn(name = "submission_check_url_id")
    private SubmissionCheckUrl submissionCheckUrl;

    @JsonIgnoreProperties({"assignments"})
    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseEntity course;

    @JsonIgnoreProperties({"createdAssignemnts"})
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity author;

    @JsonIgnoreProperties({"assignment"})
    @OneToMany(mappedBy = "assignment")
    private List<SubmissionEntity> submissions;

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

    public String getInputDataType() {
        return inputDataType;
    }

    public void setInputDataType(String inputDataType) {
        this.inputDataType = inputDataType;
    }

    public Integer getOutputDataType() {
        return outputDataType;
    }

    public void setOutputDataType(Integer outputDataType) {
        this.outputDataType = outputDataType;
    }

    public Integer getSubmissionNotify() {
        return submissionNotify;
    }

    public void setSubmissionNotify(Integer submissionNotify) {
        this.submissionNotify = submissionNotify;
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

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public SubmissionCheckEntity getSubmissionCheckType() {
        return submissionCheckType;
    }

    public void setSubmissionCheckType(SubmissionCheckEntity submissionCheckType) {
        this.submissionCheckType = submissionCheckType;
    }

    public SubmissionCheckUrl getSubmissionCheckUrl() {
        return submissionCheckUrl;
    }

    public void setSubmissionCheckUrl(SubmissionCheckUrl submissionCheckUrl) {
        this.submissionCheckUrl = submissionCheckUrl;
    }

    public CourseEntity getCourse() {
        return course;
    }

    public void setCourse(CourseEntity course) {
        this.course = course;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    public List<SubmissionEntity> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<SubmissionEntity> submissions) {
        this.submissions = submissions;
    }
}
