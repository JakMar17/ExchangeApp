package si.fri.jakmar.backend.exchangeapp.api.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.dtos.assignments.AssignmentDTO;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessUnauthorizedException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.DataNotFoundException;
import si.fri.jakmar.backend.exchangeapp.services.assignments.AssignmentsServices;

import java.util.logging.Logger;

@RestController
@RequestMapping("/assignment")
@CrossOrigin("*")
public class AssignmentApi {

    private final Logger logger = Logger.getLogger(AssignmentApi.class.getSimpleName());
    @Autowired
    private AssignmentsServices assignmentsServices;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllAssignmentsOfCourse(@AuthenticationPrincipal UserEntity userEntity, @RequestParam Integer courseId) throws AccessUnauthorizedException, DataNotFoundException, AccessForbiddenException {
        return ResponseEntity.ok(assignmentsServices.getBasicDataForAssignmentsOfCourse(userEntity.getPersonalNumber(), courseId));
    }

    @GetMapping
    public ResponseEntity<Object> getAssignment(@AuthenticationPrincipal UserEntity userEntity, @RequestParam Integer assignmentId) throws AccessUnauthorizedException, DataNotFoundException, AccessForbiddenException {
        return ResponseEntity.ok(assignmentsServices.getAssignmentsData(userEntity.getPersonalNumber(), assignmentId));
    }

    @GetMapping("/detailed")
    public ResponseEntity<Object> getAssignmentWithSubmission(@AuthenticationPrincipal UserEntity userEntity, @RequestParam Integer assignmentId) throws AccessForbiddenException, DataNotFoundException, AccessUnauthorizedException {
        var x = assignmentsServices.getAssignmentWithSubmissions(userEntity.getPersonalNumber(), assignmentId);
        return ResponseEntity.ok(x);
    }

    @PostMapping("/save")
    public ResponseEntity<Object> saveAssignment(@AuthenticationPrincipal UserEntity userEntity, Integer courseId, @RequestBody AssignmentDTO data) throws AccessForbiddenException, DataNotFoundException {
        return ResponseEntity.ok(assignmentsServices.insertOrUpdateAssignment(userEntity.getPersonalNumber(), courseId, data));
    }

    @PutMapping("/set-visibility")
    public ResponseEntity<Object> setAssignmentsVisibility(@AuthenticationPrincipal UserEntity userEntity, @RequestParam Integer assignmentId, @RequestParam Boolean visibility) throws AccessForbiddenException, DataNotFoundException {
        return ResponseEntity.ok(assignmentsServices.setVisibility(userEntity.getPersonalNumber(), assignmentId, visibility));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteAssignment(@AuthenticationPrincipal UserEntity userEntity, @RequestParam Integer assignmentId) throws AccessForbiddenException, DataNotFoundException {
        assignmentsServices.deleteAssignment(userEntity.getPersonalNumber(), assignmentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/archive")
    public ResponseEntity<Object> archiveAssignment(@AuthenticationPrincipal UserEntity userEntity, @RequestBody AssignmentDTO assignmentDTO) throws AccessForbiddenException, DataNotFoundException {
        return ResponseEntity.ok(assignmentsServices.setArchivedStatus(userEntity.getPersonalNumber(), assignmentDTO.getAssignmentId(), assignmentDTO.getArchived()));
    }
}
