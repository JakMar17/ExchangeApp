package si.fri.jakmar.backend.exchangeapp.api.courses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.fri.jakmar.backend.exchangeapp.api.exceptions.ExceptionWrapper;
import si.fri.jakmar.backend.exchangeapp.database.repositories.course.CourseRepository;
import si.fri.jakmar.backend.exchangeapp.exceptions.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.exceptions.AccessUnauthorizedException;
import si.fri.jakmar.backend.exchangeapp.exceptions.DataNotFoundException;
import si.fri.jakmar.backend.exchangeapp.services.courses.CoursesServices;

import java.util.logging.Logger;

@RestController
@RequestMapping("courses/")
@CrossOrigin("*")
public class CoursesApi {

    private final Logger logger = Logger.getLogger(CoursesApi.class.getSimpleName());
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    private CoursesServices coursesServices;

    @GetMapping("all")
    public ResponseEntity<Object> getCourses() throws DataNotFoundException {
        return ResponseEntity.ok(coursesServices.getAllCoursesWithBasicInfo());
    }

    @GetMapping("my")
    public ResponseEntity<Object> getUsersCourses(@RequestHeader(name = "Personal-Number") String personalNumber) throws DataNotFoundException {
        return ResponseEntity.ok(coursesServices.getAllCoursesOfUserWithBasicInfo(personalNumber));
    }

    @GetMapping("course")
    public ResponseEntity<Object> getCourse(@RequestParam Integer courseId, @RequestHeader(name = "Personal-Number") String personalNumber) {
        try {
            return ResponseEntity.ok(coursesServices.getCourseData(courseId, personalNumber));
        } catch (DataNotFoundException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionWrapper(e));
        } catch (AccessForbiddenException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(coursesServices.getCourseBasicData(courseId));
        } catch (AccessUnauthorizedException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(coursesServices.getCourseBasicData(courseId));
        }
    }

    @GetMapping("course/access")
    public ResponseEntity<Object> checkPasswordAndGetCourse(@RequestParam Integer courseId, @RequestHeader(name = "Personal-Number") String personalNumber, @RequestHeader(name = "Course-Password") String coursePassword) {
        try {
            return ResponseEntity.ok(coursesServices.checkPasswordAndGetCourse(courseId, personalNumber, coursePassword));
        } catch (DataNotFoundException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionWrapper(e));
        } catch (AccessForbiddenException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(coursesServices.getCourseBasicData(courseId));
        } catch (AccessUnauthorizedException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(coursesServices.getCourseBasicData(courseId));
        }
    }
}
