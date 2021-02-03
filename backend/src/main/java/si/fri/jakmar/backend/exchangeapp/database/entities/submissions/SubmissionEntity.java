package si.fri.jakmar.backend.exchangeapp.database.entities.submissions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import si.fri.jakmar.backend.exchangeapp.database.entities.assignments.AssignmentEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.purchases.PurchaseEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "submission")
public class SubmissionEntity {

    @Id
    @Column(name = "submission_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "input_id")
    private String input;
    @Column(name = "output_id")
    private String output;
    @Column(name = "submission_created")
    private Timestamp created = new Timestamp(System.currentTimeMillis());

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity authour;

    @JsonIgnoreProperties({"submissions"})
    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private AssignmentEntity assignemnt;

    @ManyToOne
    @JoinColumn(name = "submission_status_id")
    private SubmissionStatusEntity status;

    @JsonIgnoreProperties({"submissionBought"})
    @OneToMany(mappedBy = "submissionBought")
    private List<PurchaseEntity> purchases;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public UserEntity getAuthour() {
        return authour;
    }

    public void setAuthour(UserEntity authour) {
        this.authour = authour;
    }

    public AssignmentEntity getAssignemnt() {
        return assignemnt;
    }

    public void setAssignemnt(AssignmentEntity assignemnt) {
        this.assignemnt = assignemnt;
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
}
