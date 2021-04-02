package si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.SubmissionEntity;

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

    public SubmissionSimilarityEntity(Double cosineSimilarityInput, Double jaccardIndexInput, Double sorenseDiceCoefficientInput, Double ratcliffObershelfInput, Double cosineSimilarityOutput, Double jaccardIndexOutput, Double sorenseDiceCoefficientOutput, Double ratcliffObershelfOutput, SubmissionEntity submission1, SubmissionEntity submission2) {
        this.cosineSimilarityInput = cosineSimilarityInput;
        this.cosineSimilarityOutput = cosineSimilarityOutput;
        this.cosineSimilarity = this.cosineSimilarityInput * this.cosineSimilarityOutput;

        this.jaccardIndexInput = jaccardIndexInput;
        this.jaccardIndexOutput = jaccardIndexOutput;
        this.jaccardIndex = this.jaccardIndexInput * this.jaccardIndexOutput;

        this.sorenseDiceCoefficientInput = sorenseDiceCoefficientInput;
        this.sorenseDiceCoefficientOutput = sorenseDiceCoefficientOutput;
        this.sorenseDiceCoefficient = this.sorenseDiceCoefficientInput * this.sorenseDiceCoefficientOutput;

        this.ratcliffObershelfInput = ratcliffObershelfInput;
        this.ratcliffObershelfOutput = ratcliffObershelfOutput;
        this.ratcliffObershelf = this.ratcliffObershelfInput * this.ratcliffObershelfOutput;

        this.averageInput = calculateInputAverage();
        this.averageOutput = calculateOutputAverage();
        this.average = calculateAverage();

        this.submission1 = submission1;
        this.submission2 = submission2;
    }

    private Double calculateAverage() {
        Double sum = 0.0;
        int no = 0;

        if(!this.cosineSimilarity.isNaN()) {
            sum += this.cosineSimilarity;
            no++;
        }
        if(!this.jaccardIndex.isNaN()) {
            sum += this.jaccardIndex;
            no++;
        }
        if(!this.sorenseDiceCoefficient.isNaN()) {
            sum += this.sorenseDiceCoefficient;
            no++;
        }
        if(!this.ratcliffObershelf.isNaN()) {
            sum += this.ratcliffObershelf;
            no++;
        }

        return sum / no;
    }

    private Double calculateInputAverage() {
        Double sum = 0.0;
        int no = 0;

        if(!this.cosineSimilarityInput.isNaN()) {
            sum += this.cosineSimilarityInput;
            no++;
        }
        if(!this.jaccardIndexInput.isNaN()) {
            sum += this.jaccardIndexInput;
            no++;
        }
        if(!this.sorenseDiceCoefficientInput.isNaN()) {
            sum += this.sorenseDiceCoefficientInput;
            no++;
        }
        if(!this.ratcliffObershelfInput.isNaN()) {
            sum += this.ratcliffObershelfInput;
            no++;
        }

        return sum / no;
    }

    private Double calculateOutputAverage() {
        Double sum = 0.0;
        int no = 0;

        if(!this.cosineSimilarityOutput.isNaN()) {
            sum += this.cosineSimilarityOutput;
            no++;
        }
        if(!this.jaccardIndexOutput.isNaN()) {
            sum += this.jaccardIndexOutput;
            no++;
        }
        if(!this.sorenseDiceCoefficientOutput.isNaN()) {
            sum += this.sorenseDiceCoefficientOutput;
            no++;
        }
        if(!this.ratcliffObershelfOutput.isNaN()) {
            sum += this.ratcliffObershelfOutput;
            no++;
        }

        return sum / no;
    }

    public SubmissionSimilarityEntity beforeSavingToMongo() {
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
