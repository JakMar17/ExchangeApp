package si.fri.jakmar.exchangeapp.backend.testingutility.database.entities;

import org.springframework.stereotype.Service;
import si.fri.jakmar.exchangeapp.backend.testingutility.functions.TestEnvironment;

@Service
public class CorrectnessTestService {

    private final TestEnvironment testEnvironment;

    public CorrectnessTestService(TestEnvironment testEnvironment) {
        this.testEnvironment = testEnvironment;
    }
}
