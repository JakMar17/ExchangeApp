package si.fri.jakmar.exchangeapp.backend.testingutility.services;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import si.fri.jakmar.exchangeapp.backend.testingutility.constants.Constants;
import si.fri.jakmar.exchangeapp.backend.testingutility.containers.FileContainer;
import si.fri.jakmar.exchangeapp.backend.testingutility.containers.FilePairContainer;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.mongo.entities.SubmissionCorrectnessResultEntity;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.mongo.entities.TestStatus;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.mongo.repositories.SubmissionCorrectnessResultRepository;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.AssignmentSourceEntity;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.SubmissionCorrectnessStatus;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.SubmissionEntity;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.repositories.AssignmentEntityRepository;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.repositories.SubmissionEntityRepository;
import si.fri.jakmar.exchangeapp.backend.testingutility.exceptions.CreatingEnvironmentException;
import si.fri.jakmar.exchangeapp.backend.testingutility.exceptions.DataNotFoundException;
import si.fri.jakmar.exchangeapp.backend.testingutility.functions.FileFunctions;
import si.fri.jakmar.exchangeapp.backend.testingutility.functions.Randomizer;
import si.fri.jakmar.exchangeapp.backend.testingutility.functions.TestEnvironment;
import si.fri.jakmar.exchangeapp.backend.testingutility.resources.SubmissionTestResult;
import si.fri.jakmar.exchangeapp.backend.testingutility.storage.FileStorageService;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Data
@Service
public class CorrectnessTestService {

    private final TestEnvironment testEnvironment;
    private final AssignmentEntityRepository assignmentEntityRepository;
    private final SubmissionEntityRepository submissionEntityRepository;
    private final SubmissionCorrectnessResultRepository submissionCorrectnessResultRepository;
    private final FileStorageService fileStorageService;

    @Value("${att.file.path}")
    private String attFilePath;
    @Value("${environment.base.path}")
    private String testsBasePath;

    /**
     * prints from proccess output
     *
     * @param process
     * @throws IOException
     */
    private static void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }

    /**
     * creates and runs correctness test
     *
     * @param assignmentId
     * @return
     * @throws DataNotFoundException
     * @throws CreatingEnvironmentException
     */
    public Stream<SubmissionTestResult> test(Integer assignmentId) throws DataNotFoundException, CreatingEnvironmentException {
        var assignment = assignmentEntityRepository.findById(assignmentId)
                .orElseThrow(() -> new DataNotFoundException("Ne najdem naloge"));

        if(assignment.getSource() == null)
            throw new CreatingEnvironmentException("Program za preverjanje ne obstaja");

        var pairs = CollectionUtils.emptyIfNull(assignment.getSubmissions()).stream()
                .filter(e -> e.getCorrectnessStatus() == SubmissionCorrectnessStatus.PENDING_REVIEW)
                .map(e -> generatePairContainer(assignment.getInputDataType(), assignment.getOutputDataType(), e))
                .toArray(FilePairContainer[]::new);

        var testId = Randomizer.createRandomString(Stream.ofNullable(null));
        testEnvironment.create(testId, pairs, assignment.getSource());

        try {
            runTest(testId, assignment.getSource());
        } catch (IOException e) {
            e.printStackTrace();
        }

        var results = checkResult(testsBasePath + testId + "/", pairs);
        submissionCorrectnessResultRepository.saveAll(results);

        testEnvironment.clean(testId);
        return results.stream()
                //.peek(e -> submissionEntityRepository.updateWithTestResult(e.getSubmissionId(), e.getTestStatus()))
                .map(e -> new SubmissionTestResult(e.getSubmissionId(), e.getTestStatus()));
    }

    private FilePairContainer generatePairContainer(String inputExtension, String outputExtension, SubmissionEntity submission) {
        String fileKey = submission.getFileKey();
        File inputFile = fileStorageService.getFile("input_" + fileKey);
        File outputFile = fileStorageService.getFile("output_" + fileKey);

        return new FilePairContainer(fileKey, new FileContainer(inputExtension, inputFile), new FileContainer(outputExtension, outputFile), submission);
    }

    /**
     * runs bash ATT script
     *
     * @param testId name of test
     * @param source program source
     * @throws IOException error while running script
     */
    private void runTest(String testId, AssignmentSourceEntity source) throws IOException {
        System.out.println("Začenjam");
        String command = String.format(
                "bash %s %s -t %d -l %s -p %s",
                attFilePath,
                source.getProgramName(),
                source.getTimeout(),
                source.getProgramLanguage(),
                testsBasePath + testId
        );

        System.out.println(command);

        Process p = Runtime.getRuntime().exec(command);

        printResults(p);
    }

    /**
     * checks every tuple of test case input, test case output and expected result
     *
     * @param basePath test base path
     * @param tests    tests to check
     * @return list of results of checks
     */
    private List<SubmissionCorrectnessResultEntity> checkResult(String basePath, FilePairContainer[] tests) {
        String outputPath = basePath + Constants.TEST_OUTPUT_SUB_PATH;
        String expectedPath = basePath + Constants.EXPECTED_OUTPUT_SUB_PATH;
        String inputPath = basePath + Constants.TEST_INPUT_SUB_PATH;
        String diffPath = basePath + Constants.DIFF_SUB_PATH;

        var error = errorWhileCompiling(expectedPath);
        List<SubmissionCorrectnessResultEntity> results = new ArrayList<>();
        if (error.isPresent()) {
            var errorByte = FileFunctions.fileToByte(error.get());
            for (var test : tests) {
                var submission = test.getSubmission();
                results.add(SubmissionCorrectnessResultEntity.createResultWithCompileError(
                        submission.getAssignment().getId(),
                        submission.getId(),
                        TestStatus.COMPILE_ERROR,
                        errorByte
                ));
            }
        } else {
            int index = 0;
            for (var test : tests) {
                var submission = test.getSubmission();
                var input = FileFunctions.fileToByte(
                        new File(String.format("%s%s%d%s", inputPath, "test", index, submission.getAssignment().getInputDataType()))
                );
                var output = FileFunctions.fileToByte(
                        new File(String.format("%s%s%d%s", outputPath, "test", index, submission.getAssignment().getOutputDataType()))
                );
                var expectedOutput = FileFunctions.fileToByte(
                        new File(String.format("%s%s%d%s", expectedPath, "test", index, submission.getAssignment().getOutputDataType()))
                );
                var diff = FileFunctions.fileToByte(
                        new File(String.format("%s%s%d%s", diffPath, "test", index, ".txt"))
                );

                var result = diff == null ? TestStatus.TIMEOUT :
                        diff.length == 0 ? TestStatus.OK : TestStatus.NOK;

                results.add(SubmissionCorrectnessResultEntity.createResultWithoutCompileError(
                        submission.getAssignment().getId(),
                        submission.getId(),
                        result,
                        input,
                        output,
                        expectedOutput,
                        diff
                ));

                index++;
            }
        }

        return results;
    }

    /**
     * checks if there was an error while compiling source (if there is, there is file error.txt)
     *
     * @param outputPath where to look for error file
     * @return not empty Optional if there was compiling error
     */
    private Optional<File> errorWhileCompiling(String outputPath) {
        File error = new File(outputPath + "error.txt");
        if (error.exists())
            return Optional.of(error);
        else
            return Optional.empty();
    }
}
