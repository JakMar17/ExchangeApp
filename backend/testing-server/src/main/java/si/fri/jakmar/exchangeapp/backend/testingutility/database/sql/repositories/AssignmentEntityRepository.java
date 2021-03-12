package si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.repositories;

import org.springframework.data.repository.CrudRepository;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.AssignmentEntity;

public interface AssignmentEntityRepository extends CrudRepository<AssignmentEntity, Integer> {
}
