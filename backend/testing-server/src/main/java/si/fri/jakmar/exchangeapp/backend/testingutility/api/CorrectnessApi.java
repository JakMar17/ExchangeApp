package si.fri.jakmar.exchangeapp.backend.testingutility.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import si.fri.jakmar.exchangeapp.backend.testingutility.exceptions.CreatingEnvironmentException;
import si.fri.jakmar.exchangeapp.backend.testingutility.exceptions.DataNotFoundException;
import si.fri.jakmar.exchangeapp.backend.testingutility.services.CorrectnessTestService;

@RestController
@RequestMapping("/correctness/")
public class CorrectnessApi {

    @Autowired
    private CorrectnessTestService correctnessTestService;

    @GetMapping("test")
    public ResponseEntity testCorrectnessOfAssignment(@RequestParam Integer assignmentId) throws CreatingEnvironmentException, DataNotFoundException {
        var result = correctnessTestService.test(assignmentId);
        return ResponseEntity.ok(result);
    }
}
