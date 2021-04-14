package si.fri.jakmar.exchangeapp.backend.testingutility.api;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import si.fri.jakmar.exchangeapp.backend.testingutility.api.interfaces.CorrectnessApiInterface;
import si.fri.jakmar.exchangeapp.backend.testingutility.exceptions.CreatingEnvironmentException;
import si.fri.jakmar.exchangeapp.backend.testingutility.exceptions.DataNotFoundException;
import si.fri.jakmar.exchangeapp.backend.testingutility.models.requests.TestRequestBody;
import si.fri.jakmar.exchangeapp.backend.testingutility.resources.SubmissionTestResult;
import si.fri.jakmar.exchangeapp.backend.testingutility.services.CorrectnessTestService;

import java.util.stream.Stream;

@Data
@RestController
public class CorrectnessApi implements CorrectnessApiInterface {

    private final CorrectnessTestService correctnessTestService;

//    public ResponseEntity<Stream<SubmissionTestResult>> testCorrectnessOfAssignment(Integer assignmentId) throws CreatingEnvironmentException, DataNotFoundException {
//        var result = correctnessTestService.test(assignmentId);
//        return ResponseEntity.ok(result);
//    }

    @Override
    public ResponseEntity<Stream<SubmissionTestResult>> testCorrectness(TestRequestBody testRequest) throws CreatingEnvironmentException, DataNotFoundException {
        var results = correctnessTestService.test(testRequest.getAssignmentId());
        return ResponseEntity.ok(results);
    }
}
