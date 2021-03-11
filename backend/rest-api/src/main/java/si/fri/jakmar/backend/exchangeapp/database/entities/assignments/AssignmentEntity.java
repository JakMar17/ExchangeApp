package si.fri.jakmar.backend.exchangeapp.database.entities.assignments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import si.fri.jakmar.backend.exchangeapp.database.entities.courses.CourseEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.submissions.SubmissionEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.dtos.assignments.AssignmentDTO;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Data
@NoArgsConstructor
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
    private String outputDataType;
    @Column(name = "submission_notify")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean submissionNotify;
    @Column(name = "plagiarism_warning")
    private Integer plagiarismWarning;
    @Column(name = "plagiarism_level")
    private Integer plagiarismLevel;
    @Column(name = "visible")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean visible = true;
    @Column(name = "assignment_date_created")
    private LocalDateTime created = LocalDateTime.now(ZoneOffset.UTC);
    @Column(name = "assignment_archived")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean archived = false;
    @Column(name = "assignment_deleted")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean deleted = false;
    @Column(name = "submission_check")
    @Enumerated(EnumType.STRING)
    private SubmissionCheck submissionCheck = SubmissionCheck.NONE;

    @ManyToOne
    @JoinColumn(name = "submission_check_url_id")
    private SubmissionCheckUrl submissionCheckUrl;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseEntity course;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity author;

    @OneToMany(mappedBy = "assignment")
    private List<SubmissionEntity> submissions;

    @OneToOne(mappedBy = "assignment")
    private AssignmentSourceEntity source;

    public AssignmentEntity(Integer id, String title, String classroomUrl, String description, LocalDateTime startDate, LocalDateTime endDate, Integer maxSubmissionsTotal, Integer maxSubmissionsPerStudent, Integer coinsPerSubmission, Integer coinsPrice, String inputDataType, String outputDataType, Boolean submissionNotify, Integer plagiarismWarning, Integer plagiarismLevel, Boolean visible, Boolean archived, SubmissionCheck submissionCheck, CourseEntity course, UserEntity author) {
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
        this.inputDataType = inputDataType;
        this.outputDataType = outputDataType;
        this.submissionNotify = submissionNotify;
        this.plagiarismWarning = plagiarismWarning;
        this.plagiarismLevel = plagiarismLevel;
        this.visible = visible;
        this.archived = archived;
        this.submissionCheck = submissionCheck;
        this.course = course;
        this.author = author;
    }

    public AssignmentEntity updateFromDto(AssignmentDTO dto) {
        this.title = dto.getTitle();
        this.classroomUrl = dto.getClassroomUrl();
        this.description = dto.getDescription();
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
        this.maxSubmissionsTotal = dto.getMaxSubmissionsTotal();
        this.maxSubmissionsPerStudent = dto.getMaxSubmissionsPerStudent();
        this.coinsPerSubmission = dto.getCoinsPerSubmission();
        this.coinsPrice = dto.getCoinsPrice();
        this.inputDataType = dto.getInputExtension();
        this.outputDataType = dto.getOutputExtension();
        this.submissionNotify = dto.getNotifyOnEmail();
        this.plagiarismWarning = dto.getPlagiarismWarning();
        this.plagiarismLevel = dto.getPlagiarismLevel();
        this.visible = dto.getVisible() == null || dto.getVisible();
        this.archived = dto.getArchived() != null && dto.getArchived();
        this.submissionCheck = dto.getTestType();
        return this;
    }
}
