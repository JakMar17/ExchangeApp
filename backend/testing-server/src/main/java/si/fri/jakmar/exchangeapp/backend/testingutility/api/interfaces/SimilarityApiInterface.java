package si.fri.jakmar.exchangeapp.backend.testingutility.api.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/similarity")
public interface SimilarityApiInterface {

    @GetMapping("test")
    ResponseEntity testSimilarityOfAssignment(@RequestParam Integer assignmentId) throws Exception;
}
