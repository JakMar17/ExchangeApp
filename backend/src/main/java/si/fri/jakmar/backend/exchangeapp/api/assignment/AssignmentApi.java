package si.fri.jakmar.backend.exchangeapp.api.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.fri.jakmar.backend.exchangeapp.api.exceptions.ExceptionWrapper;
import si.fri.jakmar.backend.exchangeapp.dtos.assignments.AssignmentDTO;
import si.fri.jakmar.backend.exchangeapp.services.assignments.AssignmentsServices;
import si.fri.jakmar.backend.exchangeapp.exceptions.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.exceptions.AccessUnauthorizedException;
import si.fri.jakmar.backend.exchangeapp.exceptions.DataNotFoundException;

import java.util.logging.Logger;

@RestController
@RequestMapping("/assignment")
@CrossOrigin("*")
public class AssignmentApi {

    private final Logger logger = Logger.getLogger(AssignmentApi.class.getSimpleName());
    @Autowired
    private AssignmentsServices assignmentsServices;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllAssignmentsOfCourse(@RequestHeader(name = "Personal-Number") String personalNumber, @RequestParam Integer courseId) throws AccessUnauthorizedException, DataNotFoundException, AccessForbiddenException {
        return ResponseEntity.ok(assignmentsServices.getBasicDataForAssignmentsOfCourse(personalNumber, courseId));
    }

    @GetMapping
    public ResponseEntity<Object> getAssignment(@RequestHeader(name = "Personal-Number") String personalNumber, @RequestParam Integer assignmentId) throws AccessUnauthorizedException, DataNotFoundException, AccessForbiddenException {
        return ResponseEntity.ok(assignmentsServices.getAssignmentsData(personalNumber, assignmentId));
    }

    @PostMapping("/save")
    public ResponseEntity<Object> saveAssignment(@RequestHeader(name = "Personal-Number") String personalNumber, Integer courseId, @RequestBody AssignmentDTO data) throws AccessForbiddenException, DataNotFoundException {
        return ResponseEntity.ok(assignmentsServices.insertOrUpdateAssignment(personalNumber, courseId, data));
    }

    @PutMapping("/set-visibility")
    public ResponseEntity<Object> setAssignmentsVisibility(@RequestHeader(name = "Personal-Number") String personalNumber, @RequestParam Integer assignmentId, @RequestParam Boolean visibility) throws AccessForbiddenException, DataNotFoundException {
        return ResponseEntity.ok(assignmentsServices.setVisibility(personalNumber, assignmentId, visibility));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteAssignment(@RequestHeader(name = "Personal-Number") String personalNumber, @RequestParam Integer assignmentId) throws AccessForbiddenException, DataNotFoundException {
        assignmentsServices.deleteAssignment(personalNumber, assignmentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/archive")
    public ResponseEntity<Object> archiveAssignment(@RequestHeader(name = "Personal-Number") String personalNumber, @RequestBody AssignmentDTO assignmentDTO) throws AccessForbiddenException, DataNotFoundException {
        return ResponseEntity.ok(assignmentsServices.setArchivedStatus(personalNumber, assignmentDTO.getAssignmentId(), assignmentDTO.getArchived()));
    }
}
