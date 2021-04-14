package si.fri.jakmar.exchangeapp.backend.testingutility.api.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import si.fri.jakmar.exchangeapp.backend.testingutility.exceptions.CreatingEnvironmentException;
import si.fri.jakmar.exchangeapp.backend.testingutility.exceptions.DataNotFoundException;
import si.fri.jakmar.exchangeapp.backend.testingutility.models.requests.TestRequestBody;


@RequestMapping("/correctness/")
public interface CorrectnessApiInterface {

//    @GetMapping("test")
//    @Operation(summary = "Executes correctness test for not yet tested submissions of given assignment",
//            description = "Takes assignment id and finds its submissions with status PENDING_REVIEW. " +
//                    "If any submissions are found it runs test checking if files are OK")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Tests were successfully started and executed",
//                    content = {
//                            @Content(mediaType = "application/json",
//                                    array = @ArraySchema(schema = @Schema(implementation = SubmissionTestResult.class)))
//                    }
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Assignment with given ID was not found"
//            ),
//            @ApiResponse(
//                    responseCode = "500",
//                    description = "There was an internal error while executing test"
//            ),
//    })
//    ResponseEntity testCorrectnessOfAssignment(@RequestParam Integer assignmentId) throws Exception;

    @PostMapping("/test")
    ResponseEntity<?> testCorrectness(@RequestBody TestRequestBody testRequest) throws CreatingEnvironmentException, DataNotFoundException;
}
