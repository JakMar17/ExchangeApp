package si.fri.jakmar.backend.exchangeapp.dtos.submissions;

import lombok.Data;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.submissions.SubmissionCorrectnessStatus;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.submissions.SubmissionEntity;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.submissions.SubmissionSimilarityStatus;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.dtos.users.UserDTO;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;

@Data
public class SubmissionDTO {
    private Integer submissionId;
    private String input;
    private String output;
    private LocalDateTime created;
    private UserDTO author;
    private SubmissionCorrectnessStatus correctnessStatus;
    private SubmissionSimilarityStatus similarityStatus;
    private String inputFile;
    private String outputFile;
    private String diffOrErrorMessage;
    private String expectedOutput;

    public SubmissionDTO() {
    }

    private SubmissionDTO(Integer submissionId, String input, String output, LocalDateTime created, UserDTO author, SubmissionCorrectnessStatus correctnessStatus, SubmissionSimilarityStatus similarityStatus) {
        this.submissionId = submissionId;
        this.input = input;
        this.output = output;
        this.created = created;
        this.author = author;
        this.correctnessStatus = correctnessStatus;
        this.similarityStatus = similarityStatus;
    }

    private SubmissionDTO(Integer submissionId, String input, String output, LocalDateTime created, UserDTO author, SubmissionCorrectnessStatus correctnessStatus, SubmissionSimilarityStatus similarityStatus, String inputFile, String outputFile, String expectedOutput, String diffOrErrorMessage) {
        this.submissionId = submissionId;
        this.input = input;
        this.output = output;
        this.created = created;
        this.author = author;
        this.correctnessStatus = correctnessStatus;
        this.similarityStatus = similarityStatus;
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.expectedOutput = expectedOutput;
        this.diffOrErrorMessage = diffOrErrorMessage;
    }

    public static SubmissionDTO castFromEntity(SubmissionEntity entity) {
        return castFromEntity(entity, null);
    }

    public static SubmissionDTO castFromEntity(SubmissionEntity entity, UserEntity user) {
        return new SubmissionDTO(
                entity.getId(),
                "input_" + entity.getFileKey(),
                "output_" + entity.getFileKey(),
                entity.getCreated(),
                user != null
                    ? UserDTO.castFromEntityWithoutCourses(user, false)
                    : null,
                entity.getCorrectnessStatus(),
                entity.getSimilarityStatus()
        );
    }

    public static SubmissionDTO castFromEntityDetailed(SubmissionEntity entity, File input, File output, String expectedOutput, String diffOrErrorMessage) throws IOException {
        return SubmissionDTO.castFromEntityDetailed(entity, null, input, output, expectedOutput, diffOrErrorMessage);
    }

    public static SubmissionDTO castFromEntityDetailed(SubmissionEntity entity, UserEntity user, File input, File output, String expectedOutput, String error) throws IOException {
        return new SubmissionDTO(
                entity.getId(),
                "input_" + entity.getFileKey(),
                "output_" + entity.getFileKey(),
                entity.getCreated(),
                user != null
                        ? UserDTO.castFromEntityWithoutCourses(user, false)
                        : null,
                entity.getCorrectnessStatus(),
                entity.getSimilarityStatus(),
                Files.readString(input.toPath(), StandardCharsets.UTF_8),
                Files.readString(output.toPath(), StandardCharsets.UTF_8),
                expectedOutput,
                error
        );
    }
}
