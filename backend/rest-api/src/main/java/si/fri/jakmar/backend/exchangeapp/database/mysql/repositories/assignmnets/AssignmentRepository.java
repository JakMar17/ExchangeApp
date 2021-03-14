package si.fri.jakmar.backend.exchangeapp.database.mysql.repositories.assignmnets;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.assignments.AssignmentEntity;

import java.util.List;

public interface AssignmentRepository extends CrudRepository<AssignmentEntity, Integer> {

    @Query("select a from AssignmentEntity a where a.deleted = false")
    List<AssignmentEntity> getAllNotDeleted();

    default AssignmentEntity markAsDeleted(AssignmentEntity entity) {
        entity.setDeleted(true);
        return this.save(entity);
    }
}
