package si.fri.jakmar.backend.exchangeapp.database.entities.purchases;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import si.fri.jakmar.backend.exchangeapp.database.entities.submissions.SubmissionEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "purchase")
public class PurchaseEntity {

    @Id
    @Column(name = "purchase_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "purchase_date")
    private LocalDateTime created = LocalDateTime.now(ZoneOffset.UTC);

    @JsonIgnoreProperties({"purchases"})
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userBuying;

    @JsonIgnoreProperties({"purchases"})
    @ManyToOne
    @JoinColumn(name = "submission_id")
    private SubmissionEntity submissionBought;

    public PurchaseEntity() {
    }

    public PurchaseEntity(UserEntity userBuying, SubmissionEntity submissionBought) {
        this.userBuying = userBuying;
        this.submissionBought = submissionBought;
    }

    public PurchaseEntity(Integer id, LocalDateTime created, UserEntity userBuying, SubmissionEntity submissionBought) {
        this.id = id;
        this.created = created;
        this.userBuying = userBuying;
        this.submissionBought = submissionBought;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
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
