package si.fri.jakmar.backend.exchangeapp.database.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import si.fri.jakmar.backend.exchangeapp.database.mongo.entities.SubmissionCorrectnessResultEntity;

import java.util.List;

public interface SubmissionCorrectnessResultRepository extends MongoRepository<SubmissionCorrectnessResultEntity, String> {
    List<SubmissionCorrectnessResultEntity> findBySubmissionIdOrderByCreatedDesc(Integer submissionId);
}
