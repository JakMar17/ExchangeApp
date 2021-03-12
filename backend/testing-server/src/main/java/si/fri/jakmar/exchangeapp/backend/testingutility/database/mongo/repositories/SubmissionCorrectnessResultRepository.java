package si.fri.jakmar.exchangeapp.backend.testingutility.database.mongo.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.mongo.entities.SubmissionCorrectnessResultEntity;

import java.util.List;
import java.util.stream.Stream;

public interface SubmissionCorrectnessResultRepository extends MongoRepository<SubmissionCorrectnessResultEntity, Integer> {
}
