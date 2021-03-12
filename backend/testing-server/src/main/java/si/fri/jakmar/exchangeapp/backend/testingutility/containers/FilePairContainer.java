package si.fri.jakmar.exchangeapp.backend.testingutility.containers;

import lombok.Data;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.SubmissionEntity;

@Data
public class FilePairContainer {
    private final String filename;
    private final FileContainer input;
    private final FileContainer output;
    private final SubmissionEntity submission;
}
