package si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.repositories;

import org.springframework.data.repository.CrudRepository;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.mongo.entities.TestStatus;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.AssignmentEntity;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.SubmissionCorrectnessStatus;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.SubmissionEntity;

import java.util.List;

public interface SubmissionEntityRepository extends CrudRepository<SubmissionEntity, Integer> {
    default void updateWithTestResult(Integer submissionId, TestStatus testStatus) {
        findById(submissionId).ifPresent((submission) -> {
                    submission.setCorrectnessStatus(
                            switch (testStatus) {
                                case OK -> SubmissionCorrectnessStatus.OK;
                                case NOK -> SubmissionCorrectnessStatus.NOK;
                                case TIMEOUT -> SubmissionCorrectnessStatus.TIMEOUT;
                                case COMPILE_ERROR -> SubmissionCorrectnessStatus.COMPILE_ERROR;
                            }
                    );
                    save(submission);
                }
        );
    }

    List<SubmissionEntity> findByAssignment(AssignmentEntity entity);
}
