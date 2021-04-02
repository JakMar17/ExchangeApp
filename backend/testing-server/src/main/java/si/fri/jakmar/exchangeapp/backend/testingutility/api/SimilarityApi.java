package si.fri.jakmar.exchangeapp.backend.testingutility.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import si.fri.jakmar.exchangeapp.backend.testingutility.api.interfaces.SimilarityApiInterface;
import si.fri.jakmar.exchangeapp.backend.testingutility.exceptions.DataNotFoundException;
import si.fri.jakmar.exchangeapp.backend.testingutility.models.SubmissionSimilarityDTO;
import si.fri.jakmar.exchangeapp.backend.testingutility.models.mappers.SubmissionSimilarityMapper;
import si.fri.jakmar.exchangeapp.backend.testingutility.services.SimilarityTestService;

import java.util.Arrays;

@RestController
@RequiredArgsConstructor
public class SimilarityApi implements SimilarityApiInterface {

    private final SimilarityTestService similarityTestService;
    private final SubmissionSimilarityMapper submissionSimilarityMapper;

    @Override
    public ResponseEntity<SubmissionSimilarityDTO[]> testSimilarityOfAssignment(Integer assignmentId) throws DataNotFoundException {
        var data = similarityTestService.calculateSimilarityForAssignment(assignmentId);
        return ResponseEntity.ok(
                Arrays.stream(data)
                        .map(submissionSimilarityMapper::mapFromEntity)
                        .toArray(SubmissionSimilarityDTO[]::new)
        );
    }
}
