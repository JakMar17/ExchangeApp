package si.fri.jakmar.exchangeapp.backend.testingutility.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.AssignmentEntity;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.SubmissionEntity;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.SubmissionSimilarityStatus;

@Getter
@Setter
@NoArgsConstructor
public class SubmissionDto {
    private Integer submissionId;
    private SubmissionSimilarityStatus similarityStatus;
    private SubmissionSimilarityDto[] similarityResults;

    private SubmissionDto(Integer submissionId, SubmissionSimilarityStatus similarityStatus, SubmissionSimilarityDto[] similarityResults) {
        this.submissionId = submissionId;
        this.similarityStatus = similarityStatus;
        this.similarityResults = similarityResults;
    }

    public static SubmissionDto createDtoWithSimilarityResult(SubmissionEntity entity, AssignmentEntity assignment, SubmissionSimilarityDto[] results) {
        SubmissionSimilarityStatus status = SubmissionSimilarityStatus.NOT_TESTED;
        double maxPercentage = 0;
        for (var o : results)
            if (o.getAverage() > maxPercentage)
                maxPercentage = o.getAverage();
        maxPercentage *= 100;

        if (results.length == 0)
            status = SubmissionSimilarityStatus.NOT_TESTED;
        else if (assignment.getPlagiarismLevel() != null && maxPercentage >= assignment.getPlagiarismLevel())
            status = SubmissionSimilarityStatus.NOK;
        else if (assignment.getPlagiarismWarning() != null && maxPercentage >= assignment.getPlagiarismWarning())
            status = SubmissionSimilarityStatus.WARNING;
        else
            status = SubmissionSimilarityStatus.OK;

        return new SubmissionDto(entity.getId(), status, results);
    }
}
