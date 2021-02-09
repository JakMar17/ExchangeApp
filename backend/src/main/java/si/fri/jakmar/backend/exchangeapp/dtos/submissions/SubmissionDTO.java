package si.fri.jakmar.backend.exchangeapp.dtos.submissions;

import si.fri.jakmar.backend.exchangeapp.database.entities.submissions.SubmissionEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.dtos.users.UserDTO;

import java.time.LocalDateTime;

public class SubmissionDTO {
    private Integer submissionId;
    private String input;
    private String output;
    private LocalDateTime created;
    private UserDTO author;
    private String status;

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

    public static SubmissionDTO castFromEntity(SubmissionEntity entity) {
        return castFromEntity(entity, null);
    }

    public static SubmissionDTO castFromEntity(SubmissionEntity entity, UserEntity user) {
        return new SubmissionDTO(
                entity.getId(),
                entity.getInput(),
                entity.getOutput(),
                entity.getCreated(),
                user != null
                    ? UserDTO.castFromEntityWithoutCourses(user, false)
                    : null,
                entity.getStatus().getStatus()
        );
    }

    public Integer getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Integer submissionId) {
        this.submissionId = submissionId;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
