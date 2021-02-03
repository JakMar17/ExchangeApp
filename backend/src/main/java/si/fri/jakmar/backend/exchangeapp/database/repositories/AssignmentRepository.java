package si.fri.jakmar.backend.exchangeapp.database.repositories;

import org.springframework.data.repository.CrudRepository;
import si.fri.jakmar.backend.exchangeapp.database.entities.assignments.AssignmentEntity;

public interface AssignmentRepository extends CrudRepository<AssignmentEntity, Integer> {
}
