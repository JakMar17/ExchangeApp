package si.fri.jakmar.backend.exchangeapp.database.entities.submissions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import si.fri.jakmar.backend.exchangeapp.database.entities.assignments.AssignmentEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.purchases.PurchaseEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "submission_status_id")
    private SubmissionStatusEntity status = new SubmissionStatusEntity(1);

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

    public SubmissionEntity(Integer id, String fileKey, LocalDateTime created, UserEntity author, AssignmentEntity assignment, SubmissionStatusEntity status, List<PurchaseEntity> purchases) {
        this.id = id;
        this.fileKey = fileKey;
        this.created = created;
        this.author = author;
        this.assignment = assignment;
        this.status = status;
        this.purchases = purchases;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String input) {
        this.fileKey = input;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    public AssignmentEntity getAssignment() {
        return assignment;
    }

    public void setAssignment(AssignmentEntity assignment) {
        this.assignment = assignment;
    }

    public SubmissionStatusEntity getStatus() {
        return status;
    }

    public void setStatus(SubmissionStatusEntity status) {
        this.status = status;
    }

    public List<PurchaseEntity> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<PurchaseEntity> purchases) {
        this.purchases = purchases;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
