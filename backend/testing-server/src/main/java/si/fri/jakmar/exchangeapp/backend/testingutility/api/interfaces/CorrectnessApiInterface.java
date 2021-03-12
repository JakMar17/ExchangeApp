package si.fri.jakmar.exchangeapp.backend.testingutility.api.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import si.fri.jakmar.exchangeapp.backend.testingutility.resources.SubmissionTestResult;

import java.util.List;


@RequestMapping("/correctness/")
public interface CorrectnessApiInterface {

    @GetMapping("test")
    @Operation(summary = "Executes correctness test for not yet tested submissions of given assignment",
            description = "Takes assignment id and finds its submissions with status PENDING_REVIEW. " +
                          "If any submissions are found it runs test checking if files are OK")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tests were successfully started and executed",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = SubmissionTestResult.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Assignment with given ID was not found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "There was an internal error while executing test"
            ),
    })
    ResponseEntity testCorrectnessOfAssignment(@RequestParam Integer assignmentId) throws Exception;

}
