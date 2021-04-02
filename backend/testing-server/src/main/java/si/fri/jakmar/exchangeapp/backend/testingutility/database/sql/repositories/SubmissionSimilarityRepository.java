package si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.repositories;

import org.springframework.data.repository.CrudRepository;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.SubmissionSimilarityEntity;

public interface SubmissionSimilarityRepository extends CrudRepository<SubmissionSimilarityEntity, Integer> {
}
