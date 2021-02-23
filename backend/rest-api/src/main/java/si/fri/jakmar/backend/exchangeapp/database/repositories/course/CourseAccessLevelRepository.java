package si.fri.jakmar.backend.exchangeapp.database.repositories.course;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import si.fri.jakmar.backend.exchangeapp.database.entities.courses.CourseAccessLevelEntity;

import java.util.List;

public interface CourseAccessLevelRepository extends CrudRepository<CourseAccessLevelEntity, Integer> {
    @Query("SELECT c FROM CourseAccessLevelEntity c WHERE c.description LIKE :description")
    List<CourseAccessLevelEntity> getCourseAccessLevelEntitiesByDescription(String description);
}
