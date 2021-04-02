package si.fri.jakmar.exchangeapp.backend.testingutility.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.SubmissionSimilarityEntity;


@Getter
@Setter
@NoArgsConstructor
public class SubmissionSimilarityDTO {
    private Integer submissionIDCompared;

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
}
