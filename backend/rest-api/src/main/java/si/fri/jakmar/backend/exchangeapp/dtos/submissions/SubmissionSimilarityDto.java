package si.fri.jakmar.backend.exchangeapp.dtos.submissions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.submissions.SubmissionEntity;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.submissions.SubmissionSimilarityEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionSimilarityDto {
    private Double inputSimilarity;
    private Double outputSimilarity;
    private Double similarity;
    private String similarTo;

    public static SubmissionSimilarityDto castFromEntity(SubmissionSimilarityEntity submissionSimilarityEntity, SubmissionEntity submissionEntity) {
        var id = submissionEntity.getId().equals(submissionSimilarityEntity.getSubmission1().getId())
                ? submissionSimilarityEntity.getSubmission2().getId()
                : submissionSimilarityEntity.getSubmission1().getId();

        return new SubmissionSimilarityDto(
                submissionSimilarityEntity.getAverageInput() *100,
                submissionSimilarityEntity.getAverageOutput() *100,
                submissionSimilarityEntity.getAverage() *100,
                id.toString()
        );
    }
}
