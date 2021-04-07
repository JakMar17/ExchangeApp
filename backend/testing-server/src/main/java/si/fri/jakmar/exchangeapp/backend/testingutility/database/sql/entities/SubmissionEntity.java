package si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

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

    @OneToMany(mappedBy = "submission1")
    private List<SubmissionSimilarityEntity> similarities;

    @Override
    public String toString() {
        return "SubmissionEntity{" +
                "fileKey='" + fileKey + '\'' +
                ", status=" + correctnessStatus +
                '}';
    }
}
