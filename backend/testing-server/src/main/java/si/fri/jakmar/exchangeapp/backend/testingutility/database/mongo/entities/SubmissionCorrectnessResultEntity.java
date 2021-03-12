package si.fri.jakmar.exchangeapp.backend.testingutility.database.mongo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("SubmissionCorrectnessResult")
public class SubmissionCorrectnessResultEntity {
    @Id
    private String objectId;

    private Integer assignmentId;
    private Integer submissionId;
    private LocalDateTime created = LocalDateTime.now(ZoneOffset.UTC);
    @Enumerated(EnumType.STRING)
    private TestStatus testStatus;
    private byte[] input;
    private byte[] output;
    private byte[] expectedOutput;
    private byte[] diff;
    private byte[] compileError;

    private SubmissionCorrectnessResultEntity(Integer assignmentId, Integer submissionId, TestStatus testStatus, byte[] compileError) {
        this.assignmentId = assignmentId;
        this.submissionId = submissionId;
        this.testStatus = testStatus;
        this.compileError = compileError;
    }

    public static SubmissionCorrectnessResultEntity createResultWithCompileError(Integer assignmentId, Integer submissionId, TestStatus status, byte[] compileError) {
        return new SubmissionCorrectnessResultEntity(
                assignmentId,
                submissionId,
                status,
                compileError
        );
    }

    private SubmissionCorrectnessResultEntity(Integer assignmentId, Integer submissionId, TestStatus testStatus, byte[] input, byte[] output, byte[] expectedOutput, byte[] diff) {
        this.assignmentId = assignmentId;
        this.submissionId = submissionId;
        this.testStatus = testStatus;
        this.input = input;
        this.output = output;
        this.expectedOutput = expectedOutput;
        this.diff = diff;
    }

    public static SubmissionCorrectnessResultEntity createResultWithoutCompileError(Integer assignmentId, Integer submissionId, TestStatus testStatus, byte[] input, byte[] output, byte[] expectedOutput, byte[] diff) {
        return new SubmissionCorrectnessResultEntity(
                assignmentId, submissionId, testStatus, input, output, expectedOutput, diff
        );
    }
}
