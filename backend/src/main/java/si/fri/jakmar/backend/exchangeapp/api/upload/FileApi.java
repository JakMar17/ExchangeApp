package si.fri.jakmar.backend.exchangeapp.api.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import si.fri.jakmar.backend.exchangeapp.api.exceptions.ExceptionWrapper;
import si.fri.jakmar.backend.exchangeapp.database.entities.assignments.AssignmentEntity;
import si.fri.jakmar.backend.exchangeapp.dtos.assignments.AssignmentDTO;
import si.fri.jakmar.backend.exchangeapp.dtos.submissions.SubmissionDTO;
import si.fri.jakmar.backend.exchangeapp.dtos.upload.UploadDTO;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessUnauthorizedException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.DataNotFoundException;
import si.fri.jakmar.backend.exchangeapp.exceptions.FileException;
import si.fri.jakmar.backend.exchangeapp.exceptions.submissions.OverMaximumNumberOfSubmissions;
import si.fri.jakmar.backend.exchangeapp.services.assignments.AssignmentsServices;
import si.fri.jakmar.backend.exchangeapp.services.submissions.SubmissionService;
import si.fri.jakmar.backend.exchangeapp.storage.FileStorageService;

import java.io.IOException;
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

//    @PostMapping("/upload")
//    public ResponseEntity<SubmissionDTO> uploadFile(@RequestParam MultipartFile input, @RequestParam MultipartFile output, @RequestParam Integer assignmentId, @RequestHeader("Personal-Number") String personalNumber) throws FileException, AccessUnauthorizedException, DataNotFoundException, AccessForbiddenException, OverMaximumNumberOfSubmissions {
//        var data = submissionService.saveSubmission(input, output, assignmentId, personalNumber);
//        return ResponseEntity.ok(data);
//    }

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

    @GetMapping("/all")
    public ResponseEntity getAllFiles() {
        //return ResponseEntity.ok(storageService.loadAll());
        return null;
    }

    @GetMapping
    public Object getFile(@RequestParam String file) throws IOException {
        var data =  fileStorageService.getFile(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file + "\"")
                .body(data);
    }
}
