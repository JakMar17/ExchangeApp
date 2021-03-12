package si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.repositories;

import org.springframework.data.repository.CrudRepository;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.AssignmentSourceEntity;

public interface AssignmentSourceRepository extends CrudRepository<AssignmentSourceEntity, Integer> {
}
