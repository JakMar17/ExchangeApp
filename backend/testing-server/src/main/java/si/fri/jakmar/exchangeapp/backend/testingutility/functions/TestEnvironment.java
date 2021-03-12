package si.fri.jakmar.exchangeapp.backend.testingutility.functions;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import si.fri.jakmar.exchangeapp.backend.testingutility.containers.FilePairContainer;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.AssignmentSourceEntity;
import si.fri.jakmar.exchangeapp.backend.testingutility.exceptions.CreatingEnvirontmentException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class TestEnvironment {

    private static final String TEST_CASES_FOLDER_PATH = "testCases";
    private static final String INPUT_FOLDER_PATH = "input";
    private static final String DELIMITER = "/";
    private static final String TESTER_NAME = "tester.sh";

    @Value("${environment.base.path}")
    private String basePath;
    @Value("${att.file.path}")
    private String attPath;

    public void create(String testId, FilePairContainer[] inputsOutputs, AssignmentSourceEntity source) throws CreatingEnvirontmentException {
        if (!createFolder(basePath, testId))
            throw new CreatingEnvirontmentException("Cannot create base directory");

        String baseTestEnvironmentPath = basePath + testId + DELIMITER;

        if (!createFolder(baseTestEnvironmentPath, TEST_CASES_FOLDER_PATH))
            throw new CreatingEnvirontmentException("Cannot create testCases directory");
        if (!createFolder(baseTestEnvironmentPath, INPUT_FOLDER_PATH))
            throw new CreatingEnvirontmentException("Cannot create input directory");

        String testCasesPath = baseTestEnvironmentPath + TEST_CASES_FOLDER_PATH + DELIMITER;
        String inputPath = baseTestEnvironmentPath + INPUT_FOLDER_PATH + DELIMITER;

        if (!copySourceFromDatabase(source, baseTestEnvironmentPath))
            throw new CreatingEnvirontmentException("Error copying source");

        for (int i = 0; i < inputsOutputs.length; i++) {
            var pair = inputsOutputs[i];
            copyInputOutputPair(pair, inputPath, testCasesPath, i);
        }
    }

    private boolean createFolder(String basePath, String relativePath) {
        File folder = new File(basePath + relativePath);
        return folder.mkdir();
    }

    private boolean copyInputOutputPair(FilePairContainer inputOutput, String inputPath, String outputPath, int fileIndex) throws CreatingEnvirontmentException {
        File i = inputOutput.getInput().getFile();
        File o = inputOutput.getOutput().getFile();

        if (!copyFile(i, inputPath, String.format("test%d%s", fileIndex, inputOutput.getInput().getFileExtension())))
            throw new CreatingEnvirontmentException("Error copying input file: " + inputOutput.getFilename());
        if (!copyFile(o, outputPath, String.format("test%d%s", fileIndex, inputOutput.getOutput().getFileExtension())))
            throw new CreatingEnvirontmentException("Error copying output file: " + inputOutput.getFilename());

        return true;
    }

    private boolean copyFile(File file, String path) {
        return copyFile(file, path, file.getName());
    }

    private boolean copyFile(File file, String path, String filename) {
        try {
            Files.copy(file.toPath(), Path.of(path + filename));
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private boolean copySourceFromDatabase(AssignmentSourceEntity source, String path) {
        File targetFile = new File(path + DELIMITER + source.getFileName());
        try {
            OutputStream out = new FileOutputStream(targetFile);
            out.write(source.getSource());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean clean(String testId) {
        try {
            FileUtils.deleteDirectory(new File(basePath + testId));
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
