package si.fri.jakmar.backend.exchangeapp.database.mysql.repositories.course;

import org.springframework.data.repository.CrudRepository;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.courses.CourseAccessPassword;

public interface CourseAccessPasswordRepository extends CrudRepository<CourseAccessPassword, Integer> {

}
