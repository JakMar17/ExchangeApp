package si.fri.jakmar.exchangeapp.backend.testingutility.database.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.mongo.entities.SubmissionCorrectnessResultEntity;

public interface SubmissionCorrectnessResultRepository extends MongoRepository<SubmissionCorrectnessResultEntity, Integer> {
}
