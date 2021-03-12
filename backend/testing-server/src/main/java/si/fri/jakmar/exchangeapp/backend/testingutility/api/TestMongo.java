package si.fri.jakmar.exchangeapp.backend.testingutility.api;

import lombok.Data;
import org.springframework.web.bind.annotation.*;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.mongo.entities.SubmissionCorrectnessResultEntity;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.mongo.repositories.SubmissionCorrectnessResultRepository;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.mongo.repositories.TestRepository;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.repositories.AssignmentSourceRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Data
@RestController
@RequestMapping("/mongo")
public class TestMongo {

    private final TestRepository testRepository;
    private final SubmissionCorrectnessResultRepository submissionCorrectnessResultRepository;
    private final AssignmentSourceRepository assignmentSourceRepository;

    @PostMapping("/test")
    public void save(@RequestBody SubmissionCorrectnessResultEntity entity) {
        var assignment = assignmentSourceRepository.findById(entity.getAssignmentId()).get();
        entity.setInput(assignment.getSource());
        submissionCorrectnessResultRepository.save(entity);
    }

    @GetMapping
    public List<SubmissionCorrectnessResultEntity> get(@RequestParam Integer submissionId) {
        var x = submissionCorrectnessResultRepository.findBySubmissionId(submissionId);

        String s = new String(x.get(0).getInput(), StandardCharsets.UTF_8);

        return x;
    }
}
