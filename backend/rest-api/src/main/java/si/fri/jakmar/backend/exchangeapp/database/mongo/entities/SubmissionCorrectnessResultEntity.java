package si.fri.jakmar.backend.exchangeapp.database.mongo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.submissions.SubmissionCorrectnessStatus;

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
    private SubmissionCorrectnessStatus testStatus;
    private byte[] input;
    private byte[] output;
    private byte[] expectedOutput;
    private byte[] diff;
    private byte[] compileError;
}
