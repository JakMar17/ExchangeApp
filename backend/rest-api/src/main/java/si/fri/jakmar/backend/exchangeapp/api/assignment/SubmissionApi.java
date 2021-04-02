package si.fri.jakmar.backend.exchangeapp.api.assignment;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.dtos.submissions.SubmissionDTO;
import si.fri.jakmar.backend.exchangeapp.dtos.submissions.SubmissionSimilarityDto;
import si.fri.jakmar.backend.exchangeapp.dtos.submissions.SubmissionSimilarityNormalizedDto;
import si.fri.jakmar.backend.exchangeapp.exceptions.FileException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessUnauthorizedException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.DataNotFoundException;
import si.fri.jakmar.backend.exchangeapp.services.assignments.AssignmentsServices;
import si.fri.jakmar.backend.exchangeapp.services.submissions.SubmissionService;

import java.util.List;

@RestController
@RequestMapping("/submissions")
@RequiredArgsConstructor
public class SubmissionApi {

    private final SubmissionService submissionService;
    private final AssignmentsServices assignmentsServices;

    @GetMapping("/buy")
    public ResponseEntity<List<SubmissionDTO>> buySubmissions(@AuthenticationPrincipal UserEntity userEntity, @RequestParam Integer assignmentId, @RequestParam Integer noOfSubmissions) throws AccessUnauthorizedException, DataNotFoundException, AccessForbiddenException {
        var data = submissionService.getNSubmissionsNotFromUser(assignmentId, noOfSubmissions, userEntity.getPersonalNumber());
        return ResponseEntity.ok(data);
    }

    @GetMapping("/details")
    public ResponseEntity<SubmissionDTO> getDetailedSubmission(@AuthenticationPrincipal UserEntity userEntity, @RequestParam Integer submissionId) throws DataNotFoundException, AccessUnauthorizedException, AccessForbiddenException, FileException {
        var data = submissionService.getDetailedSubmission(userEntity.getPersonalNumber(), submissionId);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SubmissionDTO>> getAllSubmissionsOfAssignment(@AuthenticationPrincipal UserEntity userEntity, @RequestParam Integer assignmentId) throws AccessForbiddenException, DataNotFoundException {
        var data = assignmentsServices.getAllSubmissionsOfAssignment(userEntity.getPersonalNumber(), assignmentId);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/similarity")
    public ResponseEntity<SubmissionSimilarityNormalizedDto[]> getSubmissionsSimilarity(@RequestParam Integer submissionId) throws DataNotFoundException {
        return ResponseEntity.ok(submissionService.getSubmissionsSimilarity(submissionId));
    }
}
