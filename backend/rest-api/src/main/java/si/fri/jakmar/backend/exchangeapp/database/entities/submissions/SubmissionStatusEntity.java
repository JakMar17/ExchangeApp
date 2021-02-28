package si.fri.jakmar.backend.exchangeapp.database.entities.submissions;

import javax.persistence.*;

@Entity
@Table(name = "submission_status")
public class SubmissionStatusEntity {

    @Id
    @Column(name = "submission_status_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "submission_status_description")
    private String status;

    public SubmissionStatusEntity() {
    }

    public SubmissionStatusEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
