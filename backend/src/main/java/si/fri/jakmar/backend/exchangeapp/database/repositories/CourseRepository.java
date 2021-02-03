package si.fri.jakmar.backend.exchangeapp.database.repositories;

import org.springframework.data.repository.CrudRepository;
import si.fri.jakmar.backend.exchangeapp.database.entities.courses.CourseEntity;

public interface CourseRepository extends CrudRepository<CourseEntity, Integer> {
}
