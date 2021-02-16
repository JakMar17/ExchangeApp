package si.fri.jakmar.backend.exchangeapp.api.upload;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import si.fri.jakmar.backend.exchangeapp.dtos.assignments.AssignmentDTO;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessUnauthorizedException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.DataNotFoundException;
import si.fri.jakmar.backend.exchangeapp.exceptions.FileException;
import si.fri.jakmar.backend.exchangeapp.exceptions.submissions.OverMaximumNumberOfSubmissions;
import si.fri.jakmar.backend.exchangeapp.services.assignments.AssignmentsServices;
import si.fri.jakmar.backend.exchangeapp.services.submissions.SubmissionService;
import si.fri.jakmar.backend.exchangeapp.storage.FileStorageService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/files")
@CrossOrigin("*")
public class FileApi {
    private static final Logger logger = Logger.getLogger(FileApi.class.getSimpleName());

    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private AssignmentsServices assignmentsServices;


    @PostMapping("/upload")
    public ResponseEntity<AssignmentDTO> uploadFile(@RequestParam Integer assignmentId, @RequestHeader("Personal-Number") String personalNumber, @RequestParam List<MultipartFile> input, @RequestParam List<MultipartFile> output) throws AccessUnauthorizedException, DataNotFoundException, AccessForbiddenException, OverMaximumNumberOfSubmissions {
        try {
            var data = submissionService.saveSubmissions(assignmentId, personalNumber, input, output);
            return ResponseEntity.ok(data);
        } catch (FileException exception) {
            logger.warning(exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(assignmentsServices.getAssignmentWithSubmissions(personalNumber, assignmentId));
        }
    }

    @GetMapping("/download-submission")
    public ResponseEntity<Object> downloadSubmission(@RequestHeader("Personal-Number") String personalNumber, @RequestParam Integer submissionId) throws IOException, DataNotFoundException, AccessForbiddenException {
        InputStreamResource resource = new InputStreamResource(submissionService.getSubmissionFiles(personalNumber, submissionId));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"testi.zip\"")
                .body(resource);
    }

    @GetMapping("/users-submissions")
    public ResponseEntity<Object> getUsersSubmissions(@RequestHeader("Personal-Number") String personalNumber, @RequestParam Integer assignmentId) throws IOException, DataNotFoundException {
        InputStreamResource resource = new InputStreamResource(submissionService.getAllUsersSubmissionFiles(personalNumber, assignmentId));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"testi.zip\"")
                .body(resource);
    }
}
