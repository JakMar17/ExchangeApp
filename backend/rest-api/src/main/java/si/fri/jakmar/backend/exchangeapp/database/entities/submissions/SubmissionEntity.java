package si.fri.jakmar.backend.exchangeapp.database.entities.submissions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Type;
import si.fri.jakmar.backend.exchangeapp.database.entities.assignments.AssignmentEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.purchases.PurchaseEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Data
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
    private SubmissionStatus status = SubmissionStatus.PENDING_REVIEW;

    @JsonIgnoreProperties({"submissionBought"})
    @OneToMany(mappedBy = "submissionBought")
    private List<PurchaseEntity> purchases;

    public SubmissionEntity() {
    }

    public SubmissionEntity(Integer id, String fileKey, UserEntity author, AssignmentEntity assignment) {
        this.id = id;
        this.fileKey = fileKey;
        this.author = author;
        this.assignment = assignment;
    }

    public SubmissionEntity(Integer id, String fileKey, LocalDateTime created, UserEntity author, AssignmentEntity assignment, SubmissionStatus status, List<PurchaseEntity> purchases) {
        this.id = id;
        this.fileKey = fileKey;
        this.created = created;
        this.author = author;
        this.assignment = assignment;
        this.status = status;
        this.purchases = purchases;
    }

}
