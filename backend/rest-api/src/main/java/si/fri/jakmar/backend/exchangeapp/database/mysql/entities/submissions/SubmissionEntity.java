package si.fri.jakmar.backend.exchangeapp.database.mysql.entities.submissions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.assignments.AssignmentEntity;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.purchases.PurchaseEntity;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.users.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "submission")
public class SubmissionEntity {

    @Id
    @Column(name = "submission_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "file_key")
    private String fileKey;
    @Column(name = "submission_created")
    private LocalDateTime created = LocalDateTime.now(ZoneOffset.UTC);
    @Column(name = "submission_deleted")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity author;

    @JsonIgnoreProperties({"submissions"})
    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private AssignmentEntity assignment;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_correctness")
    private SubmissionCorrectnessStatus correctnessStatus = SubmissionCorrectnessStatus.PENDING_REVIEW;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_similarity")
    private SubmissionSimilarityStatus similarityStatus = SubmissionSimilarityStatus.PENDING_REVIEW;

    @JsonIgnoreProperties({"submissionBought"})
    @OneToMany(mappedBy = "submissionBought")
    private List<PurchaseEntity> purchases;

    public SubmissionEntity(Integer id, String fileKey, UserEntity author, AssignmentEntity assignment) {
        this.id = id;
        this.fileKey = fileKey;
        this.author = author;
        this.assignment = assignment;
    }

    public SubmissionEntity(Integer id, String fileKey, LocalDateTime created, UserEntity author, AssignmentEntity assignment, SubmissionCorrectnessStatus correctnessStatus, SubmissionSimilarityStatus similarityStatus, List<PurchaseEntity> purchases) {
        this.id = id;
        this.fileKey = fileKey;
        this.created = created;
        this.author = author;
        this.assignment = assignment;
        this.correctnessStatus = correctnessStatus;
        this.similarityStatus = similarityStatus;
        this.purchases = purchases;
    }

}
