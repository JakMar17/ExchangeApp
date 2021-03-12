package si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.repositories;

import org.springframework.data.repository.CrudRepository;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.mongo.entities.TestStatus;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.SubmissionEntity;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.SubmissionStatus;
import si.fri.jakmar.exchangeapp.backend.testingutility.exceptions.DataNotFoundException;

public interface SubmissionEntityRepository extends CrudRepository<SubmissionEntity, Integer> {
    default void updateWithTestResult(Integer submissionId, TestStatus testStatus) {
        findById(submissionId).ifPresent((submission) -> {
                    submission.setStatus(
                            switch (testStatus) {
                                case OK -> SubmissionStatus.OK;
                                case NOK -> SubmissionStatus.NOK;
                                case TIMEOUT -> SubmissionStatus.TIMEOUT;
                                case COMPILE_ERROR -> SubmissionStatus.COMPILE_ERROR;
                            }
                    );
                    save(submission);
                }
        );
    }
}
