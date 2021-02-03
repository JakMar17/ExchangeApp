package si.fri.jakmar.backend.exchangeapp.database.repositories;

import org.springframework.data.repository.CrudRepository;
import si.fri.jakmar.backend.exchangeapp.database.entities.submissions.SubmissionEntity;

public interface SubmissionRepository extends CrudRepository<SubmissionEntity, Integer> {
}
