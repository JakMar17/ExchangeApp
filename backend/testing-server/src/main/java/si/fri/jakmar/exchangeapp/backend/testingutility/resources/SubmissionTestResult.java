package si.fri.jakmar.exchangeapp.backend.testingutility.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.mongo.entities.TestStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionTestResult {
    private Integer submissionId;
    private TestStatus status;
}
