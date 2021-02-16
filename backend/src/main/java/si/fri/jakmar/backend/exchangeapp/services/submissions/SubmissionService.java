package si.fri.jakmar.backend.exchangeapp.services.submissions;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import si.fri.jakmar.backend.exchangeapp.database.entities.assignments.AssignmentEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.purchases.PurchaseEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.submissions.SubmissionEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.database.repositories.SubmissionRepository;
import si.fri.jakmar.backend.exchangeapp.dtos.assignments.AssignmentDTO;
import si.fri.jakmar.backend.exchangeapp.dtos.submissions.SubmissionDTO;
import si.fri.jakmar.backend.exchangeapp.dtos.upload.UploadDTO;
import si.fri.jakmar.backend.exchangeapp.exceptions.FileException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessUnauthorizedException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.DataNotFoundException;
import si.fri.jakmar.backend.exchangeapp.exceptions.submissions.OverMaximumNumberOfSubmissions;
import si.fri.jakmar.backend.exchangeapp.functions.RandomizerService;
import si.fri.jakmar.backend.exchangeapp.services.assignments.AssignmentsServices;
import si.fri.jakmar.backend.exchangeapp.services.users.PurchaseServices;
import si.fri.jakmar.backend.exchangeapp.services.users.UserAccessServices;
import si.fri.jakmar.backend.exchangeapp.services.users.UserServices;
import si.fri.jakmar.backend.exchangeapp.storage.FileStorageService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private UserServices userServices;
    @Autowired
    private AssignmentsServices assignmentsServices;
    @Autowired
    private UserAccessServices userAccessServices;
    @Autowired
    private FileStorageService storageService;
    @Autowired
    private PurchaseServices purchaseServices;

    /**
     * creates submission pairs from input/output and saves them to database/filesystem
     * @param assignmentId assignment's primary key
     * @param personalNumber user's personal number
     * @param inputs list of input files
     * @param outputs list of output files
     * @return assignment's details
     * @throws DataNotFoundException course/user/submission with given data was not found
     * @throws AccessUnauthorizedException user cannot submit to given assignment
     * @throws AccessForbiddenException user cannot submit to given assignment
     * @throws FileException exception while saving file
     * @throws OverMaximumNumberOfSubmissions user has reached maximum submissions
     */
    public AssignmentDTO saveSubmissions(Integer assignmentId, String personalNumber, List<MultipartFile> inputs, List<MultipartFile> outputs) throws DataNotFoundException, AccessUnauthorizedException, AccessForbiddenException, FileException, OverMaximumNumberOfSubmissions {
        var assignment = assignmentsServices.getAssignmentById(assignmentId);
        var user = userServices.getUserByPersonalNumber(personalNumber);
        if (assignment.getCourse() == null)
            throw new DataNotFoundException("Ne najdem predmeta");

        for(var input : inputs) {
            userCanAddSubmission(user, assignment);
            var data0 = Objects.requireNonNull(input.getOriginalFilename()).split("\\.");
            var data1 = data0[0];
            var data2 = data1.split("vhod");
            var inputFileNumber = data2[1];
            var output = outputs.stream().filter(e -> {
               var outputFileNumber = Objects.requireNonNull(e.getOriginalFilename()).split("\\.")[0].split("izhod")[1];
               return inputFileNumber.equals(outputFileNumber);
            }).findFirst().orElseThrow();

            saveSubmission(assignment, user, input, output);
            assignment = assignmentsServices.getAssignmentById(assignmentId);
        }

        var finalAssignment = assignment;
        return AssignmentDTO.castFromEntityWithSubmissions(
                assignment,
                assignment.getSubmissions().stream()
                        .filter(e -> user.equals(e.getAuthor()))
                        .map(SubmissionDTO::castFromEntity)
                        .collect(Collectors.toList()),
                user.getPurchases().stream()
                        .map(PurchaseEntity::getSubmissionBought)
                        .filter(entity -> finalAssignment.equals(entity.getAssignment()))
                        .map(SubmissionDTO::castFromEntity)
                        .collect(Collectors.toList())
            );
    }

    /**
     * saves submission of input-output pair
     * @param assignmentEntity assignment of submission
     * @param userEntity user commiting changes
     * @param input input file
     * @param output output file
     * @return newly created submission
     * @throws FileException problem while saving file
     */
    private SubmissionEntity saveSubmission(AssignmentEntity assignmentEntity, UserEntity userEntity, MultipartFile input, MultipartFile output) throws FileException {
        if (!isFileExtensionOk(input, assignmentEntity.getInputDataType()) || !isFileExtensionOk(output, assignmentEntity.getOutputDataType()))
            throw new FileException("Datoteka vsebuje napačen format");

        var randomKey = RandomizerService.createRandomString(
                StreamSupport.stream(submissionRepository.findAll().spliterator(), true)
                        .map(SubmissionEntity::getFileKey));

        var inputFileName = "input_" + randomKey;
        var outputFileName = "output_" + randomKey;

        try {
            storageService.save(input, inputFileName);
            storageService.save(output, inputFileName);
        } catch (Exception e) {
            storageService.delete(inputFileName);
            storageService.delete(outputFileName);
            throw new FileException(e.getMessage());
        }

        var submission  = new SubmissionEntity(
                null,
                randomKey,
                userEntity,
                assignmentEntity
        );

        return submissionRepository.save(submission);
    }

    /**
     * gets n submissions that are not authored by given user
     * @param assignmentId assignment's id
     * @param noOfSubmissions no of submissions to be returned
     * @param personalNumber user's personal number
     * @return list of submissions
     * @throws DataNotFoundException course/user/assignment was not found or not enough submissions
     * @throws AccessUnauthorizedException user does not have rights to perform operation
     * @throws AccessForbiddenException user does not have rights to perform operation
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

        var submissions = submissionRepository.getSubmissionsForAssignmentNotFromUser(assignment, user);
        Collections.shuffle(submissions);

        return submissions.stream()
                .limit(noOfSubmissions)
                .map(e -> purchaseServices.savePurchase(user, e))
                .map(PurchaseEntity::getSubmissionBought)
                .map(SubmissionDTO::castFromEntity)
                .collect(Collectors.toList());
    }

    /**
     * returns submission's details
     * @param personalNumber user's personal number (who performs action)
     * @param submissionId submission's id
     * @return submission dto
     * @throws DataNotFoundException data not found
     * @throws AccessUnauthorizedException user does not have rights to perform operation
     * @throws AccessForbiddenException user does not have rights to perform operation
     * @throws FileException exception with file system
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

    /**
     * checks if file's filename contains extension
     * @param file
     * @param extension
     * @return returns true if file's filename contains extension
     */
    private boolean isFileExtensionOk(MultipartFile file, String extension) {
        if(file.getOriginalFilename()== null)
            return false;
        else
            return file.getOriginalFilename().contains(extension);
    }

    /**
     * checks if given user can add submission to given assignment, if not exception is thrown
     * @param user to add submission
     * @param assignment where submission is added
     * @throws AccessUnauthorizedException user cannot submit to given assignment
     * @throws AccessForbiddenException user cannot submit to given assignment
     * @throws OverMaximumNumberOfSubmissions user cannot submit to given assignment, user reached maximum allowed submissions
     */
    private void userCanAddSubmission(UserEntity user, AssignmentEntity assignment) throws AccessUnauthorizedException, AccessForbiddenException, OverMaximumNumberOfSubmissions {
        userAccessServices.userHasAccessToCourse(user, assignment.getCourse());

        if (assignment.getMaxSubmissionsPerStudent() != null && assignment.getMaxSubmissionsPerStudent() < assignment.getSubmissions().stream().filter(e -> e.getAuthor().equals(user)).count())
            throw new OverMaximumNumberOfSubmissions("Študent je presegel maksimalno število oddaj");

        if (assignment.getMaxSubmissionsTotal() != null && assignment.getMaxSubmissionsTotal() < assignment.getSubmissions().size())
            throw new OverMaximumNumberOfSubmissions("Naloga je dosegla maksimalno število oddaj");

    }
}