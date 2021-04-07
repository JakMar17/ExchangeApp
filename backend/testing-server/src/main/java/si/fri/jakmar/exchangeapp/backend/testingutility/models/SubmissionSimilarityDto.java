package si.fri.jakmar.exchangeapp.backend.testingutility.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.SubmissionEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionSimilarityDto {
    private Integer comparedWithSubmissionId;

    private Double cosineSimilarity;
    private Double jaccardIndex;
    private Double sorenseDiceCoefficient;
    private Double ratcliffObershelf;
    private Double average;

    private Double cosineSimilarityInput;
    private Double jaccardIndexInput;
    private Double sorenseDiceCoefficientInput;
    private Double ratcliffObershelfInput;
    private Double averageInput;

    private Double cosineSimilarityOutput;
    private Double jaccardIndexOutput;
    private Double sorenseDiceCoefficientOutput;
    private Double ratcliffObershelfOutput;
    private Double averageOutput;

    public SubmissionSimilarityDto(Integer comparedWithSubmissionId, Double cosineSimilarityInput, Double jaccardIndexInput, Double sorenseDiceCoefficientInput, Double ratcliffObershelfInput, Double cosineSimilarityOutput, Double jaccardIndexOutput, Double sorenseDiceCoefficientOutput, Double ratcliffObershelfOutput) {
        this.comparedWithSubmissionId = comparedWithSubmissionId;

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
}
