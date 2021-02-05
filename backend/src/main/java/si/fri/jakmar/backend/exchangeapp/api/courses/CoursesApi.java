package si.fri.jakmar.backend.exchangeapp.api.courses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.fri.jakmar.backend.exchangeapp.api.wrappers.exceptions.ExceptionWrapper;
import si.fri.jakmar.backend.exchangeapp.database.entities.courses.CourseEntity;
import si.fri.jakmar.backend.exchangeapp.database.repositories.CourseRepository;
import si.fri.jakmar.backend.exchangeapp.services.courses.CoursesServices;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.AccessUnauthorizedException;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.DataNotFoundException;

import java.util.logging.Logger;

@RestController
@RequestMapping("courses/")
public class CoursesApi {

    @Autowired
    CourseRepository courseRepository;
    @Autowired
    private CoursesServices coursesServices;
    private Logger logger = Logger.getLogger(CoursesApi.class.getSimpleName());

    @PostMapping("save")
    public ResponseEntity<CourseEntity> insertOrUpdateCourse(@RequestBody CourseEntity course) {
        CourseEntity updated = courseRepository.save(course);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("all")
    public ResponseEntity<Object> getCourses() {
        try {
            return ResponseEntity.ok(coursesServices.getAllCoursesWithBasicInfo());
        } catch (DataNotFoundException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionWrapper(e));
        }
    }

    @GetMapping("course")
    public ResponseEntity<Object> getCourse(@RequestParam Integer courseId, @RequestParam String personalNumber) {
        try {
            return ResponseEntity.ok(coursesServices.getCourseData(courseId, personalNumber));
        } catch (DataNotFoundException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionWrapper(e));
        } catch (AccessForbiddenException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionWrapper(e));
        } catch (AccessUnauthorizedException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionWrapper(e));
        }
    }
}
