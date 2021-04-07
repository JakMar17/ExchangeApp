package si.fri.jakmar.backend.exchangeapp.database.mysql.repositories.submissions;

import org.springframework.data.repository.CrudRepository;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.submissions.SubmissionEntity;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.submissions.SubmissionSimilarityEntity;

import java.util.stream.Stream;

public interface SubmissionSimilaritiesRepository extends CrudRepository<SubmissionSimilarityEntity, Integer> {

    Stream<SubmissionSimilarityEntity> findDistinctBySubmission1OrSubmission2(SubmissionEntity submission1, SubmissionEntity submission2);

    default SubmissionSimilarityEntity saveWithoutNaN(SubmissionSimilarityEntity entity) {
        entity = entity.cleanNaN();
        return save(entity);
    }
}
