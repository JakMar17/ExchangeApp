package si.fri.jakmar.backend.exchangeapp.api.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import si.fri.jakmar.backend.exchangeapp.dtos.submissions.SubmissionDTO;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessUnauthorizedException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.DataNotFoundException;
import si.fri.jakmar.backend.exchangeapp.exceptions.FileException;
import si.fri.jakmar.backend.exchangeapp.exceptions.submissions.OverMaximumNumberOfSubmissions;
import si.fri.jakmar.backend.exchangeapp.services.submissions.SubmissionService;
import si.fri.jakmar.backend.exchangeapp.storage.FileStorageService;

import java.io.IOException;
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

    @PostMapping("/upload")
    public ResponseEntity<SubmissionDTO> uploadFile(@RequestParam MultipartFile input, @RequestParam MultipartFile output, @RequestParam Integer assignmentId, @RequestHeader("Personal-Number") String personalNumber) throws FileException, AccessUnauthorizedException, DataNotFoundException, AccessForbiddenException, OverMaximumNumberOfSubmissions {
        var data = submissionService.saveSubmission(input, output, assignmentId, personalNumber);
        return ResponseEntity.ok(data);
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
