package si.fri.jakmar.backend.exchangeapp.client.testing_utility.containers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionCorrectnessResult {
    private Integer submissionId;
    private SubmissionCorrectnessStatus status;
}
