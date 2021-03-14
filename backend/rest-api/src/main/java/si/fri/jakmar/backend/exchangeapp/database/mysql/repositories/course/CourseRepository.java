package si.fri.jakmar.backend.exchangeapp.database.mysql.repositories.course;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.courses.CourseEntity;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends CrudRepository<CourseEntity, Integer> {
    @Query("select c from CourseEntity c where c.deleted = false")
    List<CourseEntity> getAllNotDeleted();

    @Query("select c from CourseEntity c where c.courseId = :id and c.deleted = false")
    Optional<CourseEntity> findByIdNotDeleted(int id);

    default CourseEntity markAsDeleted(CourseEntity course) {
        course.setDeleted(true);
        return save(course);
    }
}
