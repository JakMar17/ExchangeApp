package si.fri.jakmar.backend.exchangeapp.api.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.fri.jakmar.backend.exchangeapp.dtos.submissions.SubmissionDTO;
import si.fri.jakmar.backend.exchangeapp.exceptions.FileException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessUnauthorizedException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.DataNotFoundException;
import si.fri.jakmar.backend.exchangeapp.services.submissions.SubmissionService;

import java.util.List;

@RestController
@RequestMapping("/submissions")
@CrossOrigin("*")
public class SubmissionApi {

    @Autowired
    private SubmissionService submissionService;

    @GetMapping("/buy")
    public ResponseEntity<List<SubmissionDTO>> buySubmissions(@RequestHeader("Personal-Number") String personalNumber, @RequestParam Integer assignmentId, @RequestParam Integer noOfSubmissions) throws AccessUnauthorizedException, DataNotFoundException, AccessForbiddenException {
        var data = submissionService.getNSubmissionsNotFromUser(assignmentId, noOfSubmissions, personalNumber);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/details")
    public ResponseEntity<SubmissionDTO> getDetailedSubmission(@RequestHeader("Personal-Number") String personalNumber, @RequestParam Integer submissionId) throws DataNotFoundException, AccessUnauthorizedException, AccessForbiddenException, FileException {
        var data = submissionService.getDetailedSubmission(personalNumber, submissionId);
        return ResponseEntity.ok(data);
    }
}
