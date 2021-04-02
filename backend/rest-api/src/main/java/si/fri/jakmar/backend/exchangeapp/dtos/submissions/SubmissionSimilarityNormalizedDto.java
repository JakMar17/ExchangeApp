package si.fri.jakmar.backend.exchangeapp.dtos.submissions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionSimilarityNormalizedDto {
    private Integer inputSimilarity;
    private Integer outputSimilarity;
    private Integer noOfSubmissionsInGroup;
    private String group;

    public static SubmissionSimilarityNormalizedDto[][] createNormalizedArray() {
        var array = new SubmissionSimilarityNormalizedDto[21][21];

        for (int i = 0; i <= 20; i++)
            for (int j = 0; j <= 20; j++) {
                int per = (int) (Math.round((i*5 / 100.0) * (j*5 / 100.0) * 100.0));
                array[i][j] = new SubmissionSimilarityNormalizedDto(
                        i * 5, j * 5, 0, String.format("%d", per)
                );
            }

        return array;
    }
}
