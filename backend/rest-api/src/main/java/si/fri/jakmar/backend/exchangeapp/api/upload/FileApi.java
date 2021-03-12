package si.fri.jakmar.backend.exchangeapp.api.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import si.fri.jakmar.backend.exchangeapp.containers.DoubleWrapper;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.dtos.assignments.AssignmentDTO;
import si.fri.jakmar.backend.exchangeapp.exceptions.FileException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessUnauthorizedException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.DataNotFoundException;
import si.fri.jakmar.backend.exchangeapp.exceptions.submissions.OverMaximumNumberOfSubmissions;
import si.fri.jakmar.backend.exchangeapp.services.assignments.AssignmentsServices;
import si.fri.jakmar.backend.exchangeapp.services.submissions.SubmissionService;
import si.fri.jakmar.backend.exchangeapp.files.FileStorageService;

import java.io.ByteArrayInputStream;
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


    @PostMapping("/upload")
    public ResponseEntity<AssignmentDTO> uploadFile(@RequestParam Integer assignmentId, @AuthenticationPrincipal UserEntity userEntity, @RequestParam List<MultipartFile> input, @RequestParam List<MultipartFile> output) throws AccessUnauthorizedException, DataNotFoundException, AccessForbiddenException, OverMaximumNumberOfSubmissions, FileException {
        var data = submissionService.saveSubmissions(assignmentId, userEntity.getPersonalNumber(), input, output);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/upload-source")
    public ResponseEntity<Object> uploadSourceCode(
            @AuthenticationPrincipal UserEntity user,
            @RequestParam Integer assignmentId,
            @RequestParam String programName,
            @RequestParam String programLanguage,
            @RequestParam Integer timeout,
            @RequestParam MultipartFile source)
    throws IOException, DataNotFoundException {
        assignmentsServices.saveSource(assignmentId, programName, programLanguage, timeout, source, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/download-source")
    public ResponseEntity<Object> downloadSource(@RequestParam Integer assignmentId) throws DataNotFoundException {
        DoubleWrapper<String, ByteArrayInputStream> wrapper = assignmentsServices.downloadSource(assignmentId);
        InputStreamResource resource = new InputStreamResource(wrapper.second);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", wrapper.first));
        headers.add("File-Name", wrapper.first);
        headers.add("Access-Control-Expose-Headers", "File-Name");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .headers(headers)
                .body(resource);
    }

    @GetMapping("/download-submission")
    public ResponseEntity<Object> downloadSubmission(@AuthenticationPrincipal UserEntity userEntity, @RequestParam Integer submissionId) throws IOException, DataNotFoundException, AccessForbiddenException {
        InputStreamResource resource = new InputStreamResource(submissionService.getSubmissionFiles(userEntity.getPersonalNumber(), submissionId));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"testi.zip\"")
                .body(resource);
    }

    @GetMapping("/users-submissions")
    public ResponseEntity<Object> getUsersSubmissions(@AuthenticationPrincipal UserEntity userEntity, @RequestParam Integer assignmentId) throws IOException, DataNotFoundException {
        InputStreamResource resource = new InputStreamResource(submissionService.getAllUsersSubmissionFiles(userEntity.getPersonalNumber(), assignmentId));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"testi.zip\"")
                .body(resource);
    }
}
