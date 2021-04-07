package si.fri.jakmar.backend.exchangeapp.client.testing_utility.models.correctenss;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.submissions.SubmissionCorrectnessStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionCorrectnessResult {
    private Integer submissionId;
    private SubmissionCorrectnessStatus status;
}
