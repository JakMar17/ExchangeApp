package si.fri.jakmar.backend.exchangeapp.database.mysql.entities.submissions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import si.fri.jakmar.backend.exchangeapp.client.testing_utility.models.similarity.SimilaritiesForSubmission;

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

    private SubmissionSimilarityEntity(Double cosineSimilarity, Double jaccardIndex, Double sorenseDiceCoefficient, Double ratcliffObershelf, Double average, Double cosineSimilarityInput, Double jaccardIndexInput, Double sorenseDiceCoefficientInput, Double ratcliffObershelfInput, Double averageInput, Double cosineSimilarityOutput, Double jaccardIndexOutput, Double sorenseDiceCoefficientOutput, Double ratcliffObershelfOutput, Double averageOutput, SubmissionEntity submission1, SubmissionEntity submission2) {
        this.cosineSimilarity = cosineSimilarity;
        this.jaccardIndex = jaccardIndex;
        this.sorenseDiceCoefficient = sorenseDiceCoefficient;
        this.ratcliffObershelf = ratcliffObershelf;
        this.average = average;
        this.cosineSimilarityInput = cosineSimilarityInput;
        this.jaccardIndexInput = jaccardIndexInput;
        this.sorenseDiceCoefficientInput = sorenseDiceCoefficientInput;
        this.ratcliffObershelfInput = ratcliffObershelfInput;
        this.averageInput = averageInput;
        this.cosineSimilarityOutput = cosineSimilarityOutput;
        this.jaccardIndexOutput = jaccardIndexOutput;
        this.sorenseDiceCoefficientOutput = sorenseDiceCoefficientOutput;
        this.ratcliffObershelfOutput = ratcliffObershelfOutput;
        this.averageOutput = averageOutput;
        this.submission1 = submission1;
        this.submission2 = submission2;
    }

    public static SubmissionSimilarityEntity castFromTestingUtilityDto(SimilaritiesForSubmission similarities, SubmissionEntity submission1, SubmissionEntity submission2) {
        return new SubmissionSimilarityEntity(
                similarities.getCosineSimilarity(),
                similarities.getJaccardIndex(),
                similarities.getSorenseDiceCoefficient(),
                similarities.getRatcliffObershelf(),
                similarities.getAverage(),
                similarities.getCosineSimilarityInput(),
                similarities.getJaccardIndexInput(),
                similarities.getSorenseDiceCoefficientInput(),
                similarities.getRatcliffObershelfInput(),
                similarities.getAverageInput(),
                similarities.getCosineSimilarityOutput(),
                similarities.getJaccardIndexOutput(),
                similarities.getSorenseDiceCoefficientOutput(),
                similarities.getRatcliffObershelfOutput(),
                similarities.getAverageOutput(),
                submission1, submission2
        );
    }

    public SubmissionSimilarityEntity cleanNaN() {
        if(this.cosineSimilarity.isNaN())
            this.cosineSimilarity = null;
        if(this.jaccardIndex.isNaN())
            this.jaccardIndex = null;
        if(this.sorenseDiceCoefficient.isNaN())
            this.sorenseDiceCoefficient = null;
        if(this.ratcliffObershelf.isNaN())
            this.ratcliffObershelf = null;
        if(this.average.isNaN())
            this.average = null;
        if(this.cosineSimilarityInput.isNaN())
            this.cosineSimilarityInput = null;
        if(this.jaccardIndexInput.isNaN())
            this.jaccardIndexInput = null;
        if(this.sorenseDiceCoefficientInput.isNaN())
            this.sorenseDiceCoefficientInput = null;
        if(this.ratcliffObershelfInput.isNaN())
            this.ratcliffObershelfInput = null;
        if(this.averageInput.isNaN())
            this.averageInput = null;
        if(this.cosineSimilarityOutput.isNaN())
            this.cosineSimilarityOutput = null;
        if(this.jaccardIndexOutput.isNaN())
            this.jaccardIndexOutput = null;
        if(this.sorenseDiceCoefficientOutput.isNaN())
            this.sorenseDiceCoefficientOutput = null;
        if(this.ratcliffObershelfOutput.isNaN())
            this.ratcliffObershelfOutput = null;
        if(this.averageOutput.isNaN())
            this.averageOutput = null;

        return this;
    }
}
