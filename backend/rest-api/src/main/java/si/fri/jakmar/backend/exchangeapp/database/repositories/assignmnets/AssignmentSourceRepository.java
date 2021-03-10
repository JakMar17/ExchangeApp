package si.fri.jakmar.backend.exchangeapp.database.repositories.assignmnets;

import org.springframework.data.repository.CrudRepository;
import si.fri.jakmar.backend.exchangeapp.database.entities.assignments.AssignmentSourceEntity;

public interface AssignmentSourceRepository extends CrudRepository<AssignmentSourceEntity, Integer> {
}
