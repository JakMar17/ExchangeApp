package si.fri.jakmar.backend.exchangeapp.api.courses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.database.mysql.repositories.course.CourseRepository;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessUnauthorizedException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.DataNotFoundException;
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
    public ResponseEntity<Object> getUsersCourses(@AuthenticationPrincipal UserEntity userEntity) throws DataNotFoundException {
        return ResponseEntity.ok(coursesServices.getAllCoursesOfUserWithBasicInfo(userEntity.getPersonalNumber()));
    }

    @GetMapping("course")
    public ResponseEntity<Object> getCourse(@RequestParam Integer courseId, @AuthenticationPrincipal UserEntity userEntity) throws AccessUnauthorizedException, DataNotFoundException, AccessForbiddenException {
        return ResponseEntity.ok(coursesServices.getCourseData(courseId, userEntity.getPersonalNumber()));

    }

    @GetMapping("course/access")
    public ResponseEntity<Object> checkPasswordAndGetCourse(@RequestParam Integer courseId, @AuthenticationPrincipal UserEntity userEntity, @RequestHeader(name = "Course-Password") String coursePassword) throws AccessUnauthorizedException, DataNotFoundException, AccessForbiddenException {
        return ResponseEntity.ok(coursesServices.checkPasswordAndGetCourse(courseId, userEntity.getPersonalNumber(), coursePassword));
    }
}
