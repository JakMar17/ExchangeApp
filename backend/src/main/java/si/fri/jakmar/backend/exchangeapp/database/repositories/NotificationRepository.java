package si.fri.jakmar.backend.exchangeapp.database.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import si.fri.jakmar.backend.exchangeapp.database.entities.assignments.AssignmentEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.notifications.NotificationEntity;

import java.util.List;

public interface NotificationRepository extends CrudRepository<NotificationEntity, Integer> {
    @Query("select n from NotificationEntity n where n.deleted = false")
    List<NotificationEntity> getAllNotDeleted();

    @Query("select n from NotificationEntity n where n.course is null and n.deleted = false ")
    List<NotificationEntity> getDashboardNotifications();
}
