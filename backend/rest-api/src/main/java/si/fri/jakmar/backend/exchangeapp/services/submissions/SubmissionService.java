package si.fri.jakmar.backend.exchangeapp.services.submissions;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import si.fri.jakmar.backend.exchangeapp.client.testing_utility.TestingUtilityRestClient;
import si.fri.jakmar.backend.exchangeapp.client.testing_utility.models.request.TestRequest;
import si.fri.jakmar.backend.exchangeapp.database.mongo.repositories.SubmissionCorrectnessResultRepository;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.assignments.AssignmentEntity;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.assignments.SubmissionCheck;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.purchases.PurchaseEntity;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.submissions.SubmissionCorrectnessStatus;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.submissions.SubmissionEntity;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.submissions.SubmissionSimilarityEntity;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.submissions.SubmissionSimilarityStatus;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.database.mysql.repositories.submissions.SubmissionRepository;
import si.fri.jakmar.backend.exchangeapp.database.mysql.repositories.submissions.SubmissionSimilaritiesRepository;
import si.fri.jakmar.backend.exchangeapp.dtos.assignments.AssignmentDTO;
import si.fri.jakmar.backend.exchangeapp.dtos.submissions.SubmissionDTO;
import si.fri.jakmar.backend.exchangeapp.dtos.submissions.SubmissionSimilarityNormalizedDto;
import si.fri.jakmar.backend.exchangeapp.exceptions.FileException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessUnauthorizedException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.DataNotFoundException;
import si.fri.jakmar.backend.exchangeapp.exceptions.submissions.OverMaximumNumberOfSubmissions;
import si.fri.jakmar.backend.exchangeapp.files.FileStorageService;
import si.fri.jakmar.backend.exchangeapp.functions.RandomizerService;
import si.fri.jakmar.backend.exchangeapp.functions.ZipperFunction;
import si.fri.jakmar.backend.exchangeapp.services.assignments.AssignmentsServices;
import si.fri.jakmar.backend.exchangeapp.services.users.PurchaseServices;
import si.fri.jakmar.backend.exchangeapp.services.users.UserAccessServices;
import si.fri.jakmar.backend.exchangeapp.services.users.UserServices;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final SubmissionCorrectnessResultRepository submissionCorrectnessResultRepository;
    private final SubmissionSimilaritiesRepository submissionSimilaritiesRepository;
    private final UserServices userServices;
    private final AssignmentsServices assignmentsServices;
    private final UserAccessServices userAccessServices;
    private final FileStorageService storageService;
    private final PurchaseServices purchaseServices;
    private final FileStorageService fileStorageService;
    private final TestingUtilityRestClient testingUtilityRestClient;

    /**
     * creates submission pairs from input/output and saves them to database/filesystem
     *
     * @param assignmentId   assignment's primary key
     * @param personalNumber user's personal number
     * @param inputs         list of input files
     * @param outputs        list of output files
     * @return assignment's details
     * @throws DataNotFoundException          course/user/submission with given data was not found
     * @throws AccessUnauthorizedException    user cannot submit to given assignment
     * @throws AccessForbiddenException       user cannot submit to given assignment
     * @throws FileException                  exception while saving file
     * @throws OverMaximumNumberOfSubmissions user has reached maximum submissions
     */
    public AssignmentDTO saveSubmissions(Integer assignmentId, String personalNumber, List<MultipartFile> inputs, List<MultipartFile> outputs) throws DataNotFoundException, AccessUnauthorizedException, AccessForbiddenException, FileException, OverMaximumNumberOfSubmissions {
        var assignment = assignmentsServices.getAssignmentById(assignmentId);
        var user = userServices.getUserByPersonalNumber(personalNumber);
        if (assignment.getCourse() == null)
            throw new DataNotFoundException("Ne najdem predmeta");
        userAccessServices.userHasAccessToCourse(user, assignment.getCourse());

        List<InputOutputPairWrapper> inputOutputPairs = InputOutputPairWrapper.generateWrapperListFromInputsOutputs(inputs, outputs);
        Integer maxSubmissionsUserCanAdd = maxSubmissionsUserCanAdd(user, assignment);
        int noOfSubmissionsToAdd;
        if (maxSubmissionsUserCanAdd == null || maxSubmissionsUserCanAdd >= inputOutputPairs.size())
            noOfSubmissionsToAdd = inputOutputPairs.size();
        else
            noOfSubmissionsToAdd = maxSubmissionsUserCanAdd;

        List<SubmissionEntity> oldSubmissions = assignment.getSubmissions();
        List<SubmissionEntity> newSubmissions = new ArrayList<>();

        for (int i = 0; i < noOfSubmissionsToAdd; i++) {
            var pair = inputOutputPairs.get(i);
            newSubmissions.add(saveSubmission(assignment, user, pair.getInput(), pair.getOutput()));
        }

        if (assignment.getSubmissionCheck() == SubmissionCheck.AUTOMATIC) {
            var results = testingUtilityRestClient.runCorrectnessTestForAssignment(
                    new TestRequest(assignmentId, null)
            );
            for (var result : results) {
                boolean doOld = true;

                for (var newSubmission : newSubmissions) {
                    if (result.getSubmissionId().equals(newSubmission.getId())) {
                        newSubmission.setCorrectnessStatus(result.getStatus());
                        doOld = false;
                    }
                }

                if (doOld) {
                    for (var oldSubmission : oldSubmissions) {
                        if (result.getSubmissionId().equals(oldSubmission.getId())) {
                            oldSubmission.setCorrectnessStatus(result.getStatus());
                        }
                    }
                }
            }
        } else if (assignment.getSubmissionCheck() == SubmissionCheck.NONE) {
            newSubmissions.forEach(e -> e.setCorrectnessStatus(SubmissionCorrectnessStatus.OK));
        }

        if (assignment.getPlagiarismWarning() != null || assignment.getPlagiarismLevel() != null) {
            newSubmissions.forEach(e -> e.setSimilarityStatus(SubmissionSimilarityStatus.NOT_TESTED));
            var results = testingUtilityRestClient.runSimilarityTestForAssignment(
                    new TestRequest(assignment.getId(), newSubmissions.stream()
                            .filter(e -> e.getCorrectnessStatus() == SubmissionCorrectnessStatus.OK)
                            .map(SubmissionEntity::getId).toArray(Integer[]::new)
                    )
            );

            List<SubmissionSimilarityEntity> similarities = new ArrayList<>();
            for (var result : results) {
                for (var newSubmission : newSubmissions) {
                    if (result.getSubmissionId().equals(newSubmission.getId())) {
                        newSubmission.setSimilarityStatus(result.getSimilarityStatus());

                        for (var submissionCompared : result.getSimilarityResults()) {
                            var submission2 = submissionRepository.findById(submissionCompared.getComparedWithSubmissionId());
                            submission2.ifPresent(submissionEntity ->
                                    similarities.add(SubmissionSimilarityEntity.castFromTestingUtilityDto(submissionCompared, newSubmission, submissionEntity))
                            );
                        }
                    }
                }
            }

            submissionSimilaritiesRepository.saveAll(similarities.stream()
                    .map(SubmissionSimilarityEntity::cleanNaN)
                    .collect(Collectors.toList()));
        } else {
            newSubmissions.forEach(e -> e.setSimilarityStatus(SubmissionSimilarityStatus.OK));
        }

        List<SubmissionEntity> allSubmissions = new ArrayList<>();
        allSubmissions.addAll(oldSubmissions);
        allSubmissions.addAll(newSubmissions);

        assignment.setSubmissions(allSubmissions);
        submissionRepository.saveAll(allSubmissions);


        if (noOfSubmissionsToAdd < inputOutputPairs.size())
            throw new OverMaximumNumberOfSubmissions(
                    String.format("Število oddanih testnih primerov (%d), presega maksimalno dovoljeno število testnih primerov (%d). Razlika (%d) ni bila shranjena.",
                            inputOutputPairs.size(), noOfSubmissionsToAdd, inputOutputPairs.size() - noOfSubmissionsToAdd
                    )
            );


        return AssignmentDTO.castFromEntityWithSubmissions(
                assignment,
                assignment.getSubmissions().stream()
                        .filter(e -> user.equals(e.getAuthor()))
                        .map(SubmissionDTO::castFromEntity)
                        .collect(Collectors.toList()),
                user.getPurchases().stream()
                        .map(PurchaseEntity::getSubmissionBought)
                        .filter(entity -> assignment.equals(entity.getAssignment()))
                        .map(SubmissionDTO::castFromEntity)
                        .collect(Collectors.toList())
        );
    }

    /**
     * saves submission of input-output pair
     *
     * @param assignmentEntity assignment of submission
     * @param userEntity       user commiting changes
     * @param input            input file
     * @param output           output file
     * @return newly created submission
     * @throws FileException problem while saving file
     */
    private SubmissionEntity saveSubmission(AssignmentEntity assignmentEntity, UserEntity userEntity, MultipartFile input, MultipartFile output) throws FileException {
        if (!isFileExtensionOk(input, assignmentEntity.getInputDataType()) || !isFileExtensionOk(output, assignmentEntity.getOutputDataType()))
            throw new FileException("Datoteka vsebuje napačen format");

        var randomKey = RandomizerService.createRandomString(
                submissionRepository.findAll().parallelStream()
                        .map(SubmissionEntity::getFileKey));

        var inputFileName = "input_" + randomKey;
        var outputFileName = "output_" + randomKey;

        try {
            storageService.save(input, inputFileName);
            storageService.save(output, outputFileName);
        } catch (Exception e) {
            e.printStackTrace();
            storageService.delete(inputFileName);
            storageService.delete(outputFileName);
            throw new FileException(e.getMessage());
        }

        var submission = new SubmissionEntity(
                null,
                randomKey,
                userEntity,
                assignmentEntity
        );

        return submissionRepository.save(submission);
    }

    /**
     * gets n submissions that are not authored by given user
     *
     * @param assignmentId    assignment's id
     * @param noOfSubmissions no of submissions to be returned
     * @param personalNumber  user's personal number
     * @return list of submissions
     * @throws DataNotFoundException       course/user/assignment was not found or not enough submissions
     * @throws AccessUnauthorizedException user does not have rights to perform operation
     * @throws AccessForbiddenException    user does not have rights to perform operation
     */
    public List<SubmissionDTO> getNSubmissionsNotFromUser(Integer assignmentId, int noOfSubmissions, String personalNumber) throws DataNotFoundException, AccessUnauthorizedException, AccessForbiddenException {
        var assignment = assignmentsServices.getAssignmentById(assignmentId);
        var user = userServices.getUserByPersonalNumber(personalNumber);
        if (assignment.getCourse() == null)
            throw new DataNotFoundException("Ne najdem predmeta");
        userAccessServices.userHasAccessToCourse(user, assignment.getCourse());

        int coins = userServices.getUsersCoinsInCourse(user, assignment.getCourse());
        if (coins < noOfSubmissions * assignment.getCoinsPrice())
            throw new AccessForbiddenException("Uporabnik nima dovolj žetonov");

        var boughtSubmissionsOfThisAssignment = CollectionUtils.emptyIfNull(user.getPurchases())
                .stream()
                .map(PurchaseEntity::getSubmissionBought)
                .filter(entity -> entity.getAssignment().equals(assignment))
                .collect(Collectors.toSet());

        var submissions = CollectionUtils.emptyIfNull(submissionRepository.getSubmissionsForAssignmentNotFromUser(assignment, user))
                .stream()
                .filter(e -> !boughtSubmissionsOfThisAssignment.contains(e))
                .filter(e -> e.getCorrectnessStatus() == SubmissionCorrectnessStatus.OK)
                .collect(Collectors.toList());
        Collections.shuffle(submissions);

        var list = submissions.stream()
                .limit(noOfSubmissions)
                .map(e -> purchaseServices.savePurchase(user, e))
                .map(PurchaseEntity::getSubmissionBought)
                .map(SubmissionDTO::castFromEntity)
                .collect(Collectors.toList());

        if (list.isEmpty())
            throw new DataNotFoundException("V tem trenutku ne obstajajo testni primeri, ki si jih ne bi že lastili");
        else
            return list;
    }

    /**
     * returns submission's details
     *
     * @param personalNumber user's personal number (who performs action)
     * @param submissionId   submission's id
     * @return submission dto
     * @throws DataNotFoundException       data not found
     * @throws AccessUnauthorizedException user does not have rights to perform operation
     * @throws AccessForbiddenException    user does not have rights to perform operation
     * @throws FileException               exception with file system
     */
    public SubmissionDTO getDetailedSubmission(String personalNumber, Integer submissionId) throws DataNotFoundException, AccessUnauthorizedException, AccessForbiddenException, FileException {
        var user = userServices.getUserByPersonalNumber(personalNumber);
        var submission = submissionRepository.findById(submissionId).orElseThrow(() -> new DataNotFoundException("Ne najdem oddaje"));

        var assignment = submission.getAssignment();
        var course = assignment.getCourse();

        userAccessServices.userHasAccessToCourse(user, course);

        var input = storageService.getFile("input_" + submission.getFileKey());
        var output = storageService.getFile("output_" + submission.getFileKey());

        String diffOrError = null;
        String expectedOutput = null;
        if (assignment.getSubmissionCheck() == SubmissionCheck.AUTOMATIC &&
                (submission.getCorrectnessStatus() == SubmissionCorrectnessStatus.NOK || submission.getCorrectnessStatus() == SubmissionCorrectnessStatus.COMPILE_ERROR)
        ) {
            var results = submissionCorrectnessResultRepository.findBySubmissionIdOrderByCreatedDesc(submissionId);

            if (results.size() > 0) {
                var result = results.get(0);

                if (result.getExpectedOutput() != null)
                    expectedOutput = new String(result.getExpectedOutput(), StandardCharsets.UTF_8);
                if (submission.getCorrectnessStatus() == SubmissionCorrectnessStatus.NOK && result.getDiff() != null)
                    diffOrError = new String(result.getDiff(), StandardCharsets.UTF_8);
                else if (submission.getCorrectnessStatus() == SubmissionCorrectnessStatus.COMPILE_ERROR && result.getCompileError() != null)
                    diffOrError = new String(result.getCompileError(), StandardCharsets.UTF_8);
            }
        }

        try {
            return SubmissionDTO.castFromEntityDetailed(submission, input, output, expectedOutput, diffOrError);
        } catch (IOException e) {
            throw new FileException("napaka");
        }
    }

    public ByteArrayInputStream getAllUsersSubmissionFiles(String personalNumber, Integer assignmentId) throws DataNotFoundException, IOException {
        var user = userServices.getUserByPersonalNumber(personalNumber);
        var assignment = assignmentsServices.getAssignmentById(assignmentId);

        var inputFiles = CollectionUtils.emptyIfNull(assignment.getSubmissions()).stream()
                .filter(e -> e.getAuthor().equals(user))
                .map(e -> fileStorageService.getFile("input_" + e.getFileKey()))
                .collect(Collectors.toList());

        var outputFiles = CollectionUtils.emptyIfNull(assignment.getSubmissions()).stream()
                .filter(e -> e.getAuthor().equals(user))
                .map(e -> fileStorageService.getFile("output_" + e.getFileKey()))
                .collect(Collectors.toList());

        return ZipperFunction.createZip(inputFiles, assignment.getInputDataType(), outputFiles, assignment.getOutputDataType());
    }

    public ByteArrayInputStream getSubmissionFiles(String personalNumber, Integer submissionId) throws DataNotFoundException, AccessForbiddenException, IOException {
        var user = userServices.getUserByPersonalNumber(personalNumber);
        var submission = submissionRepository.findById(submissionId).orElseThrow(() -> new DataNotFoundException("Ne najdem oddaje"));

        //check if user can download submission
        if (
                !submission.getAuthor().equals(user)
                        && CollectionUtils.emptyIfNull(user.getPurchases()).stream()
                        .map(PurchaseEntity::getSubmissionBought)
                        .noneMatch(e -> e.equals(submission))
        ) {
            throw new AccessForbiddenException("Uporabnik nima pravice za prenos oddaje");
        }

        var inputFile = fileStorageService.getFile("input_" + submission.getFileKey());
        var outputFile = fileStorageService.getFile("output_" + submission.getFileKey());
        var assignment = submission.getAssignment();

        return ZipperFunction.createZip(List.of(inputFile), assignment.getInputDataType(), List.of(outputFile), assignment.getOutputDataType());
    }

    @Transactional
    public SubmissionSimilarityNormalizedDto[] getSubmissionsSimilarity(Integer submissionId) throws DataNotFoundException {
        var submission = submissionRepository.findById(submissionId).orElseThrow(() -> new DataNotFoundException("Ne najdem oddaje"));
        var array = SubmissionSimilarityNormalizedDto.createNormalizedArray();

        submissionSimilaritiesRepository.findDistinctBySubmission1OrSubmission2(submission, submission)
                .filter(e -> e.getAverageInput() != null && e.getAverageOutput() != null)
                .forEach(e -> {
                    int i = (int) ((Math.round(e.getAverageInput() * 100 / 5)));
                    int o = (int) ((Math.round(e.getAverageOutput() * 100 / 5)));
                    array[i][o].setNoOfSubmissionsInGroup(array[i][o].getNoOfSubmissionsInGroup() + 1);
                });

        return Arrays.stream(array)
                .flatMap(Stream::of)
                .filter(e -> !e.getNoOfSubmissionsInGroup().equals(0))
                .toArray(SubmissionSimilarityNormalizedDto[]::new);
    }

    /**
     * checks if file's filename contains extension
     *
     * @param file
     * @param extension
     * @return returns true if file's filename contains extension
     */
    private boolean isFileExtensionOk(MultipartFile file, String extension) {
        if (file.getOriginalFilename() == null)
            return false;
        else
            return file.getOriginalFilename().contains(extension);
    }

    /**
     * checks if given user can add submission to given assignment, if not exception is thrown
     *
     * @param user       to add submission
     * @param assignment where submission is added
     * @throws AccessUnauthorizedException    user cannot submit to given assignment
     * @throws AccessForbiddenException       user cannot submit to given assignment
     * @throws OverMaximumNumberOfSubmissions user cannot submit to given assignment, user reached maximum allowed submissions
     */
    private void userCanAddSubmission(UserEntity user, AssignmentEntity assignment) throws AccessUnauthorizedException, AccessForbiddenException, OverMaximumNumberOfSubmissions {
        userAccessServices.userHasAccessToCourse(user, assignment.getCourse());

        if (assignment.getMaxSubmissionsPerStudent() != null && assignment.getMaxSubmissionsPerStudent() < assignment.getSubmissions().stream().filter(e -> e.getAuthor().equals(user)).count())
            throw new OverMaximumNumberOfSubmissions("Študent je presegel maksimalno število oddaj");

        if (assignment.getMaxSubmissionsTotal() != null && assignment.getMaxSubmissionsTotal() < assignment.getSubmissions().size())
            throw new OverMaximumNumberOfSubmissions("Naloga je dosegla maksimalno število oddaj");

    }

    private Integer maxSubmissionsUserCanAdd(UserEntity userEntity, AssignmentEntity assignmentEntity) {
        /*
         * max total | max student
         * null      | null        | null
         * null      | non null    | max student - students
         * non null  | null        | max total - submissions
         * non null  | non null    | min(max total - submissions, max student - students)
         */

        if (assignmentEntity.getMaxSubmissionsTotal() == null) {
            if (assignmentEntity.getMaxSubmissionsPerStudent() == null) {
                return null;
            } else {
                return assignmentEntity.getMaxSubmissionsPerStudent() -
                        Math.toIntExact(assignmentEntity.getSubmissions().stream()
                                .filter(e -> e.getAuthor().equals(userEntity))
                                .count());
            }
        } else {
            if (assignmentEntity.getMaxSubmissionsPerStudent() == null) {
                return assignmentEntity.getMaxSubmissionsTotal() -
                        assignmentEntity.getSubmissions().size();
            } else {
                return Math.min(
                        assignmentEntity.getMaxSubmissionsPerStudent() -
                                Math.toIntExact(assignmentEntity.getSubmissions().stream()
                                        .filter(e -> e.getAuthor().equals(userEntity))
                                        .count()),
                        assignmentEntity.getMaxSubmissionsTotal() -
                                assignmentEntity.getSubmissions().size()
                );
            }
        }
    }
}
