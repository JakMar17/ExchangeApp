package si.fri.jakmar.backend.exchangeapp.database.entities.assignments;

import javax.persistence.*;

@Entity
@Table(name = "submission_check")
public class SubmissionCheckEntity {

    @Id
    @Column(name = "submission_check_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "submission_check_description")
    private String description;

    public SubmissionCheckEntity() {
    }

    public SubmissionCheckEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
