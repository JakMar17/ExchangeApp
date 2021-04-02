package si.fri.jakmar.backend.exchangeapp.database.mysql.entities.submissions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "submission_similarity")
public class SubmissionSimilarityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "similarity_id")
    private Integer id;

    @Column(name = "cosine_similarity")
    private Double cosineSimilarity;
    @Column(name = "jaccard_index")
    private Double jaccardIndex;
    @Column(name = "sorense_dice_coefficient")
    private Double sorenseDiceCoefficient;
    @Column(name = "ratcliff_obershelf")
    private Double ratcliffObershelf;
    @Column(name = "avg")
    private Double average;

    @Column(name = "cosine_similarity_input")
    private Double cosineSimilarityInput;
    @Column(name = "jaccard_index_input")
    private Double jaccardIndexInput;
    @Column(name = "sorense_dice_coefficient_input")
    private Double sorenseDiceCoefficientInput;
    @Column(name = "ratcliff_obershelf_input")
    private Double ratcliffObershelfInput;
    @Column(name = "avg_input")
    private Double averageInput;

    @Column(name = "cosine_similarity_output")
    private Double cosineSimilarityOutput;
    @Column(name = "jaccard_index_output")
    private Double jaccardIndexOutput;
    @Column(name = "sorense_dice_coefficient_output")
    private Double sorenseDiceCoefficientOutput;
    @Column(name = "ratcliff_obershelf_output")
    private Double ratcliffObershelfOutput;
    @Column(name = "avg_output")
    private Double averageOutput;

    @Column(name = "created_at")
    private LocalDateTime created = LocalDateTime.now(ZoneOffset.UTC);

    @ManyToOne
    @JoinColumn(name = "submission1_id")
    private SubmissionEntity submission1;

    @ManyToOne
    @JoinColumn(name = "submission2_id")
    private SubmissionEntity submission2;
}
