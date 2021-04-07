package si.fri.jakmar.exchangeapp.backend.testingutility.api.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.fri.jakmar.exchangeapp.backend.testingutility.models.SubmissionDto;
import si.fri.jakmar.exchangeapp.backend.testingutility.models.SubmissionSimilarityDto;
import si.fri.jakmar.exchangeapp.backend.testingutility.models.requests.TestRequestBody;

@RequestMapping("/similarity")
public interface SimilarityApiInterface {

    @PostMapping ("test")
    ResponseEntity<SubmissionDto[]> testSimilarityOfAssignment(@RequestBody TestRequestBody body) throws Exception;
}
