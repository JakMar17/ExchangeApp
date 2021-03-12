package si.fri.jakmar.backend.exchangeapp.services.submissions;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import si.fri.jakmar.backend.exchangeapp.client.testing_utility.TestingUtilityRestClient;
import si.fri.jakmar.backend.exchangeapp.database.entities.assignments.AssignmentEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.purchases.PurchaseEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.submissions.SubmissionEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.submissions.SubmissionStatus;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.database.repositories.SubmissionRepository;
import si.fri.jakmar.backend.exchangeapp.dtos.assignments.AssignmentDTO;
import si.fri.jakmar.backend.exchangeapp.dtos.submissions.SubmissionDTO;
import si.fri.jakmar.backend.exchangeapp.exceptions.FileException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessUnauthorizedException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.DataNotFoundException;
import si.fri.jakmar.backend.exchangeapp.exceptions.submissions.OverMaximumNumberOfSubmissions;
import si.fri.jakmar.backend.exchangeapp.functions.RandomizerService;
import si.fri.jakmar.backend.exchangeapp.functions.ZipperFunction;
import si.fri.jakmar.backend.exchangeapp.services.assignments.AssignmentsServices;
import si.fri.jakmar.backend.exchangeapp.services.users.PurchaseServices;
import si.fri.jakmar.backend.exchangeapp.services.users.UserAccessServices;
import si.fri.jakmar.backend.exchangeapp.services.users.UserServices;
import si.fri.jakmar.backend.exchangeapp.files.FileStorageService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
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

        var submissions = assignment.getSubmissions();
        for (int i = 0; i < noOfSubmissionsToAdd; i++) {
            var pair = inputOutputPairs.get(i);
            submissions.add(saveSubmission(assignment, user, pair.getInput(), pair.getOutput()));
        }

        if(noOfSubmissionsToAdd < inputOutputPairs.size())
            throw new OverMaximumNumberOfSubmissions(
                    String.format("Število oddanih testnih primerov (%d), presega maksimalno dovoljeno število testnih primerov (%d). Razlika (%d) ni bila shranjena.",
                            inputOutputPairs.size(), noOfSubmissionsToAdd, inputOutputPairs.size() - noOfSubmissionsToAdd
                        )
            );

        assignment.setSubmissions(submissions);

        testingUtilityRestClient.runCorrectnessTestForAssignment(assignment);

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
                .filter(e -> e.getStatus() == SubmissionStatus.OK)
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
        var submissionOptional = submissionRepository.findById(submissionId);
        if (submissionOptional.isEmpty())
            throw new DataNotFoundException("Ne najdem oddaje");
        var submission = submissionOptional.get();
        var assignment = submission.getAssignment();
        var course = assignment.getCourse();

        userAccessServices.userHasAccessToCourse(user, course);

        var input = storageService.getFile("input_" + submission.getFileKey());
        var output = storageService.getFile("output_" + submission.getFileKey());


        try {
            return SubmissionDTO.castFromEntityDetailed(submission, input, output);
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
