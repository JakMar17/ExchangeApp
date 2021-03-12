package si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "submission_status")
public class SubmissionStatusEntity {
    @Id
    @Column(name = "submission_status_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "submission_status_description")
    private String status;

    public SubmissionStatusEntity(Integer id) {
        this.id = id;
    }
}
