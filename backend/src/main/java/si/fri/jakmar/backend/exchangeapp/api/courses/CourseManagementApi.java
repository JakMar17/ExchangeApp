package si.fri.jakmar.backend.exchangeapp.api.courses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.fri.jakmar.backend.exchangeapp.api.courses.request_wrappers.CourseSaveRequestWrapper;
import si.fri.jakmar.backend.exchangeapp.services.courses.CoursesServices;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.DataNotFoundException;

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
    ) {
        try {
            return ResponseEntity.ok(coursesServices.getCourseDetailed(courseId, personalNumber));
        } catch (DataNotFoundException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (AccessForbiddenException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping
    public ResponseEntity<Object> saveCourse(
            @RequestHeader(name = "Personal-Number") String personalNumber,
            @RequestParam(required = false) Integer courseId,
            @RequestBody CourseSaveRequestWrapper data
    ) {
        return null;
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
