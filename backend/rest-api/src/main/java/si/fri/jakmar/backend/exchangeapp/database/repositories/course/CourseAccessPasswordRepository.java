package si.fri.jakmar.backend.exchangeapp.database.repositories.course;

import org.springframework.data.repository.CrudRepository;
import si.fri.jakmar.backend.exchangeapp.database.entities.courses.CourseAccessPassword;

public interface CourseAccessPasswordRepository extends CrudRepository<CourseAccessPassword, Integer> {

}
