package si.fri.jakmar.backend.exchangeapp.api.courses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.fri.jakmar.backend.exchangeapp.dtos.courses.CourseDTO;
import si.fri.jakmar.backend.exchangeapp.services.courses.CoursesServices;
import si.fri.jakmar.backend.exchangeapp.exceptions.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.exceptions.DataNotFoundException;

import java.util.logging.Logger;

@RestController
@RequestMapping("/course")
@CrossOrigin("*")
public class CourseManagementApi {

    private final Logger logger = Logger.getLogger(CourseManagementApi.class.getSimpleName());
    @Autowired
    private CoursesServices coursesServices;

    @GetMapping("/detailed")
    public ResponseEntity<Object> getDetailedCouresData(
            @RequestHeader(name = "Personal-Number") String personalNumber,
            @RequestParam Integer courseId
    ) throws AccessForbiddenException, DataNotFoundException {
        return ResponseEntity.ok(coursesServices.getCourseDetailed(courseId, personalNumber));
    }

    @PostMapping
    public ResponseEntity<Object> saveCourse(
            @RequestHeader(name = "Personal-Number") String personalNumber,
            @RequestBody CourseDTO data
    ) throws AccessForbiddenException, DataNotFoundException {
        var d = coursesServices.insertOrUpdateCourse(personalNumber, data);
        return ResponseEntity.ok(d);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteCourse(
            @RequestHeader(name = "Personal-Number") String personalNumber,
            @RequestParam Integer courseId
    ) {
        return null;
    }

    @PutMapping("/archive")
    public ResponseEntity<Object> archiveCourse(
            @RequestHeader(name = "Personal-Number") String personalNumber,
            @RequestParam Integer courseId
    ) {

        return null;
    }
}
