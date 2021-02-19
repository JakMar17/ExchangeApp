package si.fri.jakmar.backend.exchangeapp.database.repositories.course;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import si.fri.jakmar.backend.exchangeapp.database.entities.assignments.AssignmentEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.courses.CourseEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
