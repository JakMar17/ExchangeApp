package si.fri.jakmar.backend.exchangeapp.api.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.fri.jakmar.backend.exchangeapp.api.wrappers.exceptions.ExceptionWrapper;
import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.assignments.AssignmentDTO;
import si.fri.jakmar.backend.exchangeapp.services.assignments.AssignmentsServices;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.AccessUnauthorizedException;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.DataNotFoundException;

import java.util.logging.Logger;

@RestController
@RequestMapping("/assignment")
@CrossOrigin("*")
public class AssignmentApi {

    private final Logger logger = Logger.getLogger(AssignmentApi.class.getSimpleName());
    @Autowired
    private AssignmentsServices assignmentsServices;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllAssignmentsOfCourse(@RequestHeader(name = "Personal-Number") String personalNumber, @RequestParam Integer courseId) {
        try {
            return ResponseEntity.ok(assignmentsServices.getBasicDataForAssignmentsOfCourse(personalNumber, courseId));
        } catch (DataNotFoundException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionWrapper(e.getMessage()));
        } catch (AccessUnauthorizedException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionWrapper(e.getMessage()));
        } catch (AccessForbiddenException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionWrapper(e.getMessage()));
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Object> saveAssignment(@RequestHeader(name = "Personal-Number") String personalNumber, @RequestParam Integer courseId, @RequestBody AssignmentDTO data) {
        try {
            return ResponseEntity.ok(assignmentsServices.insertOrUpdateAssignment(personalNumber, courseId, data));
        } catch (DataNotFoundException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionWrapper(e.getMessage()));
        } catch (AccessForbiddenException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionWrapper(e.getMessage()));
        }
    }

    @PutMapping("/set-visibility")
    public ResponseEntity<Object> setAssignmentsVisibility(@RequestHeader(name = "Personal-Number") String personalNumber, @RequestParam Integer assignmentId, @RequestParam Boolean visibility) {
        try {
            return ResponseEntity.ok(assignmentsServices.setVisibility(personalNumber, assignmentId, visibility));
        } catch (DataNotFoundException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionWrapper(e.getMessage()));
        } catch (AccessForbiddenException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionWrapper(e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteAssignment(@RequestHeader(name = "Personal-Number") String personalNumber, @RequestParam Integer courseId, @RequestParam Integer assignmentId) {
        return null;
    }

    @PostMapping("/archive")
    public ResponseEntity<Object> archiveAssignment(@RequestHeader(name = "Personal-Number") String personalNumber, @RequestParam Integer courseId, @RequestParam Integer assignmentId, @RequestBody AssignmentDTO assignmentDTO) {
        return null;
    }
}
