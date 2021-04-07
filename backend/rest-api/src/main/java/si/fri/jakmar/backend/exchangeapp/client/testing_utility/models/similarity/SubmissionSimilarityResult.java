package si.fri.jakmar.backend.exchangeapp.client.testing_utility.models.similarity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.submissions.SubmissionSimilarityStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionSimilarityResult {
    private Integer submissionId;
    private SubmissionSimilarityStatus similarityStatus;
    private SimilaritiesForSubmission[] similarityResults;
}
