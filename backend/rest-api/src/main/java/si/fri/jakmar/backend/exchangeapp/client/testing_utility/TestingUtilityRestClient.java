package si.fri.jakmar.backend.exchangeapp.client.testing_utility;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import si.fri.jakmar.backend.exchangeapp.client.testing_utility.models.correctenss.SubmissionCorrectnessResult;
import si.fri.jakmar.backend.exchangeapp.client.testing_utility.models.request.TestRequest;
import si.fri.jakmar.backend.exchangeapp.client.testing_utility.models.similarity.SubmissionSimilarityResult;
import si.fri.jakmar.backend.exchangeapp.constants.UrlConstants;

@Service
@RequiredArgsConstructor
public class TestingUtilityRestClient {

    private final RestTemplate restTemplate;
    private final UrlConstants urlConstants;

    public SubmissionCorrectnessResult[] runCorrectnessTestForAssignment(TestRequest testRequest) {
        SubmissionCorrectnessResult[] results =
                restTemplate.postForObject(urlConstants.TEST_CORRECTNESS_URL , testRequest, SubmissionCorrectnessResult[].class);

        return results;
    }

    public SubmissionSimilarityResult[] runSimilarityTestForAssignment(TestRequest testRequest) {
        SubmissionSimilarityResult[] results =
                restTemplate.postForObject(urlConstants.TEST_SIMILARITY_ASSIGNMENT_URL, testRequest, SubmissionSimilarityResult[].class);
        return results;
    }

}
