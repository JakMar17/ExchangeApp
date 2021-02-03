package si.fri.jakmar.backend.exchangeapp.database.entities.purchases;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import si.fri.jakmar.backend.exchangeapp.database.entities.submissions.SubmissionEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "purchase")
public class PurchaseEntity {

    @Id
    @Column(name = "purchase_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "purchase_date")
    private Timestamp created = new Timestamp(System.currentTimeMillis());

    @JsonIgnoreProperties({"purchases"})
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userBuying;

    @JsonIgnoreProperties({"purchases"})
    @ManyToOne
    @JoinColumn(name = "submission_id")
    private SubmissionEntity submissionBought;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public UserEntity getUserBuying() {
        return userBuying;
    }

    public void setUserBuying(UserEntity userBuying) {
        this.userBuying = userBuying;
    }

    public SubmissionEntity getSubmissionBought() {
        return submissionBought;
    }

    public void setSubmissionBought(SubmissionEntity submissionBought) {
        this.submissionBought = submissionBought;
    }
}
