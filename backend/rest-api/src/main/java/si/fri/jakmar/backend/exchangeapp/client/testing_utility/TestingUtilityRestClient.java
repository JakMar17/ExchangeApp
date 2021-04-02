package si.fri.jakmar.backend.exchangeapp.client.testing_utility;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import si.fri.jakmar.backend.exchangeapp.client.testing_utility.models.SubmissionCorrectnessResult;
import si.fri.jakmar.backend.exchangeapp.client.testing_utility.models.SubmissionSimilarityResult;
import si.fri.jakmar.backend.exchangeapp.constants.UrlConstants;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.assignments.AssignmentEntity;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.submissions.SubmissionEntity;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestingUtilityRestClient {

    private final RestTemplate restTemplate;
    private final UrlConstants urlConstants;

    public SubmissionCorrectnessResult[] runCorrectnessTestForAssignment(AssignmentEntity assignment) {
        SubmissionCorrectnessResult[] results =
                restTemplate.getForObject(urlConstants.TEST_CORRECTNESS_URL , SubmissionCorrectnessResult[].class, assignment.getId());

        return results;
    }

    public SubmissionSimilarityResult[] runSimilarityTestForAssignment(AssignmentEntity assignment) {
        SubmissionSimilarityResult[] results =
                restTemplate.getForObject(urlConstants.TEST_SIMILARITY_ASSIGNMENT_URL, SubmissionSimilarityResult[].class, assignment.getId());

        return results;
    }

}
