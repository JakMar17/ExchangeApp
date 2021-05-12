package si.fri.jakmar.exchangeapp.backend.testingutility.functions;

import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import si.fri.jakmar.exchangeapp.backend.testingutility.containers.FilePairContainer;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.AssignmentSourceEntity;
import si.fri.jakmar.exchangeapp.backend.testingutility.exceptions.CreatingEnvironmentException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@Log
public class TestEnvironment {

    private static final String TEST_CASES_FOLDER_PATH = "testCases";
    private static final String INPUT_FOLDER_PATH = "input";
    private static final String DELIMITER = "/";
    private static final String TESTER_NAME = "tester.sh";

    @Value("${environment.base.path}")
    private String basePath;
    @Value("${att.file.path}")
    private String attPath;

    /**
     * creates environment for testing correctness (folder structures and copy files)
     * @param testId name of root folder for test
     * @param inputsOutputs array of tests to be run
     * @param source source program
     * @throws CreatingEnvironmentException error while creating environment
     */
    public void create(String testId, FilePairContainer[] inputsOutputs, AssignmentSourceEntity source) throws CreatingEnvironmentException {
        createFolder(basePath, "");

        if (!createFolder(basePath, testId))
            throw new CreatingEnvironmentException("Cannot create base directory");

        String baseTestEnvironmentPath = basePath + testId + DELIMITER;

        if (!createFolder(baseTestEnvironmentPath, TEST_CASES_FOLDER_PATH))
            throw new CreatingEnvironmentException("Cannot create testCases directory");
        if (!createFolder(baseTestEnvironmentPath, INPUT_FOLDER_PATH))
            throw new CreatingEnvironmentException("Cannot create input directory");

        String testCasesPath = baseTestEnvironmentPath + TEST_CASES_FOLDER_PATH + DELIMITER;
        String inputPath = baseTestEnvironmentPath + INPUT_FOLDER_PATH + DELIMITER;

        if (!copySourceFromDatabase(source, baseTestEnvironmentPath))
            throw new CreatingEnvironmentException("Error copying source");

        for (int i = 0; i < inputsOutputs.length; i++) {
            var pair = inputsOutputs[i];
            copyInputOutputPair(pair, inputPath, testCasesPath, i);
        }
    }

    /**
     * return true if it can successfully create folder on basePath + relativePath
     * @param basePath
     * @param relativePath
     * @return true if folder is created
     */
    private boolean createFolder(String basePath, String relativePath) {
        String fullPath = basePath + relativePath;
        File folder = new File(fullPath);
        folder.mkdir();

        Path path = Path.of(fullPath);
        boolean exists = Files.exists(path);
        return exists;
    }

    /**
     * copies input and output files to test environment
     * @param inputOutput file container with input/output files
     * @param inputPath path for input to be copied
     * @param outputPath path for output to be copied
     * @param fileIndex
     * @return true if files are copied successfully
     * @throws CreatingEnvironmentException error while copying files
     */
    private boolean copyInputOutputPair(FilePairContainer inputOutput, String inputPath, String outputPath, int fileIndex) throws CreatingEnvironmentException {
        File i = inputOutput.getInput().getFile();
        File o = inputOutput.getOutput().getFile();

        log.info(String.format("%s - %s \n", i.exists(), o.exists()));

        if (!copyFile(i, inputPath, String.format("test%d%s", fileIndex, inputOutput.getInput().getFileExtension())))
            throw new CreatingEnvironmentException("Error copying input file: " + inputOutput.getFilename());
        if (!copyFile(o, outputPath, String.format("test%d%s", fileIndex, inputOutput.getOutput().getFileExtension())))
            throw new CreatingEnvironmentException("Error copying output file: " + inputOutput.getFilename());

        return true;
    }

    /**
     * copies file file to path path with given filename filename
     * @param file to be copied
     * @param path where to copy file
     * @param filename what should filename be
     * @return true if copying was successful
     */
    private boolean copyFile(File file, String path, String filename) {
        try {
            var fullPath = path + filename;
            Files.copy(file.toPath(), Path.of(fullPath));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * copies source code from database to path
     * @param source database entity
     * @param path where to copy file
     * @return true if coping was successful
     */
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

    /**
     * cleans environment (deletes root folder of test and its content)
     * @param testId name of root folder to be removed
     * @return true if delete was successful
     */
    public boolean clean(String testId) {
        try {
            FileUtils.deleteDirectory(new File(basePath + testId));
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
