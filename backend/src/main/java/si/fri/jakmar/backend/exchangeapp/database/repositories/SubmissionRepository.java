package si.fri.jakmar.backend.exchangeapp.database.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import si.fri.jakmar.backend.exchangeapp.database.entities.assignments.AssignmentEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.submissions.SubmissionEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;

import java.util.List;

public interface SubmissionRepository extends CrudRepository<SubmissionEntity, Integer> {

    @Query("select s from SubmissionEntity s where s.assignment = :assignment and s.author <> :author")
    List<SubmissionEntity> getSubmissionsForAssignmentNotFromUser(AssignmentEntity assignment, UserEntity author);
}
