package si.fri.jakmar.backend.exchangeapp.client.testing_utility;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import si.fri.jakmar.backend.exchangeapp.client.testing_utility.containers.SubmissionCorrectnessResult;
import si.fri.jakmar.backend.exchangeapp.constants.UrlConstants;
import si.fri.jakmar.backend.exchangeapp.database.entities.assignments.AssignmentEntity;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestingUtilityRestClient {

    private final RestTemplate restTemplate;
    private final UrlConstants urlConstants;

    public List<SubmissionCorrectnessResult> runCorrectnessTestForAssignment(AssignmentEntity assignment) {
        SubmissionCorrectnessResult[] results =
                restTemplate.getForObject(urlConstants.TEST_CORRECTNESS_URL , SubmissionCorrectnessResult[].class, assignment.getId());

        return null;
    }
}
