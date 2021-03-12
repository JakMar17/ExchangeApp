package si.fri.jakmar.backend.exchangeapp.dtos.submissions;

import lombok.Data;
import si.fri.jakmar.backend.exchangeapp.database.entities.submissions.SubmissionEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
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
    private String status;
    private String inputFile;
    private String outputFile;

    public SubmissionDTO() {
    }

    private SubmissionDTO(Integer submissionId, String input, String output, LocalDateTime created, UserDTO author, String status) {
        this.submissionId = submissionId;
        this.input = input;
        this.output = output;
        this.created = created;
        this.author = author;
        this.status = status;
    }

    private SubmissionDTO(Integer submissionId, String input, String output, LocalDateTime created, UserDTO author, String status, String inputFile, String outputFile) {
        this.submissionId = submissionId;
        this.input = input;
        this.output = output;
        this.created = created;
        this.author = author;
        this.status = status;
        this.inputFile = inputFile;
        this.outputFile = outputFile;
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
                entity.getStatus().name()
        );
    }

    public static SubmissionDTO castFromEntityDetailed(SubmissionEntity entity, File input, File output) throws IOException {
        return SubmissionDTO.castFromEntityDetailed(entity, null, input, output);
    }

    public static SubmissionDTO castFromEntityDetailed(SubmissionEntity entity, UserEntity user, File input, File output) throws IOException {
        return new SubmissionDTO(
                entity.getId(),
                "input_" + entity.getFileKey(),
                "output_" + entity.getFileKey(),
                entity.getCreated(),
                user != null
                        ? UserDTO.castFromEntityWithoutCourses(user, false)
                        : null,
                entity.getStatus().name(),
                Files.readString(input.toPath(), StandardCharsets.UTF_8),
                Files.readString(output.toPath(), StandardCharsets.UTF_8)
        );
    }
}
