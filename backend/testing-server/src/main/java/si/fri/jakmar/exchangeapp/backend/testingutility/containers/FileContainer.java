package si.fri.jakmar.exchangeapp.backend.testingutility.containers;

import lombok.Data;

import java.io.File;

@Data
public class FileContainer {
    private final String fileExtension;
    private final File file;
}
