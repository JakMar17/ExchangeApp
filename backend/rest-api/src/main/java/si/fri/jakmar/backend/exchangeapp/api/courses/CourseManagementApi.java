package si.fri.jakmar.backend.exchangeapp.api.courses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.dtos.courses.CourseDTO;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.DataNotFoundException;
import si.fri.jakmar.backend.exchangeapp.services.courses.CoursesServices;

import java.util.logging.Logger;

@RestController
@RequestMapping("/course")
@CrossOrigin("*")
public class CourseManagementApi {

    private final Logger logger = Logger.getLogger(CourseManagementApi.class.getSimpleName());
    @Autowired
    private CoursesServices coursesServices;

    @GetMapping("/detailed")
    public ResponseEntity<CourseDTO> getDetailedCouresData(
            @AuthenticationPrincipal UserEntity userEntity,
            @RequestParam Integer courseId
    ) throws AccessForbiddenException, DataNotFoundException {
        return ResponseEntity.ok(coursesServices.getCourseDetailed(courseId, userEntity.getPersonalNumber()));
    }

    @PostMapping
    public ResponseEntity<CourseDTO> saveCourse(
            @AuthenticationPrincipal UserEntity userEntity,
            @RequestBody CourseDTO data
    ) throws AccessForbiddenException, DataNotFoundException {
        var d = coursesServices.insertOrUpdateCourse(userEntity.getPersonalNumber(), data);
        return ResponseEntity.ok(d);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCourse(
            @AuthenticationPrincipal UserEntity userEntity,
            @RequestParam Integer courseId
    ) throws AccessForbiddenException, DataNotFoundException {
        coursesServices.deleteCourse(userEntity.getPersonalNumber(), courseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/archive")
    public ResponseEntity<?> archiveCourse(
            @AuthenticationPrincipal UserEntity userEntity,
            @RequestParam Integer courseId
    ) {

        return null;
    }
}
