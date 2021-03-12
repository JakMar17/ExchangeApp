package si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Getter
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
    private Integer submissionNotify;
    @Column(name = "plagiarism_warning")
    private Integer plagiarismWarning;
    @Column(name = "plagiarism_level")
    private Integer plagiarismLevel;
    @Column(name = "visible")
    private Integer visible;
    @Column(name = "assignment_date_created")
    private LocalDateTime created = LocalDateTime.now(ZoneOffset.UTC);
    @Column(name = "assignment_archived")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean archived = false;
    @Column(name = "assignment_deleted")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean deleted = false;

    @OneToMany(mappedBy = "assignment")
    private List<SubmissionEntity> submissions;

    @OneToOne(mappedBy = "assignment")
    private AssignmentSourceEntity source;
}
