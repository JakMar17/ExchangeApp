package si.fri.jakmar.backend.exchangeapp.services.submissions;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import si.fri.jakmar.backend.exchangeapp.database.entities.assignments.AssignmentEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.purchases.PurchaseEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.submissions.SubmissionEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.database.repositories.SubmissionRepository;
import si.fri.jakmar.backend.exchangeapp.dtos.submissions.SubmissionDTO;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessUnauthorizedException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.DataNotFoundException;
import si.fri.jakmar.backend.exchangeapp.exceptions.FileException;
import si.fri.jakmar.backend.exchangeapp.exceptions.submissions.OverMaximumNumberOfSubmissions;
import si.fri.jakmar.backend.exchangeapp.functions.RandomizerService;
import si.fri.jakmar.backend.exchangeapp.services.assignments.AssignmentsServices;
import si.fri.jakmar.backend.exchangeapp.services.users.PurchaseServices;
import si.fri.jakmar.backend.exchangeapp.services.users.UserAccessServices;
import si.fri.jakmar.backend.exchangeapp.services.users.UserServices;
import si.fri.jakmar.backend.exchangeapp.storage.FileStorageService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
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

    public SubmissionDTO saveSubmission(MultipartFile input, MultipartFile output, Integer assignmentId, String personalNumber) throws DataNotFoundException, AccessUnauthorizedException, AccessForbiddenException, FileException, OverMaximumNumberOfSubmissions {
        var assignment = assignmentsServices.getAssignmentById(assignmentId);
        var user = userServices.getUserByPersonalNumber(personalNumber);
        if(assignment.getCourse() == null)
            throw new DataNotFoundException("Ne najdem predmeta");

        userCanAddSubmission(user, assignment);

        if(!isFileExtensionOk(input, assignment.getInputDataType()) || !isFileExtensionOk(output, assignment.getOutputDataType()))
            throw new FileException("Datoteka vsebuje napačen format");

        var inputFileName = RandomizerService.createRandomString(
                StreamSupport.stream(submissionRepository.findAll().spliterator(), true)
                        .map(SubmissionEntity::getInput));
        var outputFileName = RandomizerService.createRandomString(
                StreamSupport.stream(submissionRepository.findAll().spliterator(), true)
                        .map(SubmissionEntity::getOutput));

        try {
            storageService.save(input, inputFileName);
            storageService.save(output, outputFileName);
        } catch (FileException e) {
            storageService.delete(inputFileName);
            storageService.delete(outputFileName);
            throw new FileException(e.getMessage());
        }

        var entity = new SubmissionEntity(
                null,
                inputFileName,
                outputFileName,
                user,
                assignment
        );

        entity = submissionRepository.save(entity);

        return SubmissionDTO.castFromEntity(entity);
    }

    public List<SubmissionDTO> getNSubmissionsNotFromUser(Integer assignmentId, int noOfSubmissions, String personalNumber) throws DataNotFoundException, AccessUnauthorizedException, AccessForbiddenException {
        var assignment = assignmentsServices.getAssignmentById(assignmentId);
        var user = userServices.getUserByPersonalNumber(personalNumber);
        if(assignment.getCourse() == null)
            throw new DataNotFoundException("Ne najdem predmeta");
        userAccessServices.userHasAccessToCourse(user, assignment.getCourse());

        int coins = userServices.getUsersCoinsInCourse(user, assignment.getCourse());
        if(coins < noOfSubmissions * assignment.getCoinsPrice())
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

    private boolean isFileExtensionOk(MultipartFile file, String extension) {
        if(file.getOriginalFilename() == null)
            return false;
        else
            return file.getOriginalFilename().contains(extension);
    }

    private void userCanAddSubmission(UserEntity user, AssignmentEntity assignment) throws AccessUnauthorizedException, AccessForbiddenException, OverMaximumNumberOfSubmissions {
        userAccessServices.userHasAccessToCourse(user, assignment.getCourse());

        if(assignment.getMaxSubmissionsPerStudent() < assignment.getSubmissions().stream().filter(e -> e.getAuthor().equals(user)).count())
            throw new OverMaximumNumberOfSubmissions("Študent je presegel maksimalno število oddaj");

        if(assignment.getMaxSubmissionsTotal() < assignment.getSubmissions().size())
            throw new OverMaximumNumberOfSubmissions("Naloga je dosegla maksimalno število oddaj");

    }
}
