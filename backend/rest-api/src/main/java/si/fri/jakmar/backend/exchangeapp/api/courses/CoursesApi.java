package si.fri.jakmar.backend.exchangeapp.api.courses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.database.mysql.repositories.course.CourseRepository;
import si.fri.jakmar.backend.exchangeapp.dtos.courses.CourseDTO;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessUnauthorizedException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.DataNotFoundException;
import si.fri.jakmar.backend.exchangeapp.services.courses.CoursesServices;

import java.util.List;
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
    public ResponseEntity<List<CourseDTO>> getCourses() throws DataNotFoundException {
        return ResponseEntity.ok(coursesServices.getAllCoursesWithBasicInfo());
    }

    @GetMapping("my")
    public ResponseEntity<List<CourseDTO>> getUsersCourses(@AuthenticationPrincipal UserEntity userEntity) throws DataNotFoundException {
        return ResponseEntity.ok(coursesServices.getAllCoursesOfUserWithBasicInfo(userEntity.getPersonalNumber()));
    }

    @GetMapping("course")
    public ResponseEntity<CourseDTO> getCourse(@RequestParam Integer courseId, @AuthenticationPrincipal UserEntity userEntity) throws Exception {
        var successErrorContainer = coursesServices.getCourseData(courseId, userEntity.getPersonalNumber());
        if (successErrorContainer.getSuccess().isEmpty()) {
            throw new Exception();
        } else if (successErrorContainer.getError().isPresent()) {
            var error = successErrorContainer.getError().get();

            if (error instanceof AccessUnauthorizedException) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(successErrorContainer.getSuccess().get());
            } else if (error instanceof AccessForbiddenException) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(successErrorContainer.getSuccess().get());
            } else {
                throw new Exception();
            }

        } else {
            return ResponseEntity.ok(successErrorContainer.getSuccess().get());
        }
    }

    @GetMapping("course/access")
    public ResponseEntity<CourseDTO> checkPasswordAndGetCourse(@RequestParam Integer courseId, @AuthenticationPrincipal UserEntity userEntity, @RequestHeader(name = "Course-Password") String coursePassword) throws AccessUnauthorizedException, DataNotFoundException, AccessForbiddenException {
        return ResponseEntity.ok(coursesServices.checkPasswordAndGetCourse(courseId, userEntity.getPersonalNumber(), coursePassword));
    }
}
