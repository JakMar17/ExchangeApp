package si.fri.jakmar.backend.exchangeapp.services.assignments;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import si.fri.jakmar.backend.exchangeapp.containers.DoubleWrapper;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.assignments.AssignmentEntity;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.assignments.AssignmentSourceEntity;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.purchases.PurchaseEntity;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.database.mysql.repositories.assignmnets.AssignmentRepository;
import si.fri.jakmar.backend.exchangeapp.database.mysql.repositories.assignmnets.AssignmentSourceRepository;
import si.fri.jakmar.backend.exchangeapp.dtos.assignments.AssignmentDTO;
import si.fri.jakmar.backend.exchangeapp.dtos.submissions.SubmissionDTO;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessUnauthorizedException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.DataNotFoundException;
import si.fri.jakmar.backend.exchangeapp.services.courses.CoursesServices;
import si.fri.jakmar.backend.exchangeapp.services.users.UserAccessServices;
import si.fri.jakmar.backend.exchangeapp.services.users.UserServices;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AssignmentsServices {
    private final UserServices userServices;
    private final UserAccessServices userAccessServices;
    private final CoursesServices coursesServices;
    private final AssignmentRepository assignmentRepository;
    private final AssignmentSourceRepository assignmentSourceRepository;

    /**
     * return entity from database
     *
     * @param assignmentId to be find by
     * @return entity
     * @throws DataNotFoundException assignments doesn't exists
     */
    public AssignmentEntity getAssignmentById(Integer assignmentId) throws DataNotFoundException {
        var optional = assignmentRepository.findById(assignmentId);
        if (optional.isEmpty())
            throw new DataNotFoundException("Naloga s podanim ID-jem ne obstaja");
        else
            return optional.get();
    }

    /**
     * returns basic data for assignments of given course
     *
     * @param personalNumber users personal number
     * @param courseId       id for parent
     * @return list of assignments dto
     * @throws DataNotFoundException       user, course or assignments doesnt exists
     * @throws AccessUnauthorizedException user has no right to perform operation
     * @throws AccessForbiddenException    user has no right to perform operation
     */
    public List<AssignmentDTO> getBasicDataForAssignmentsOfCourse(String personalNumber, Integer courseId) throws DataNotFoundException, AccessUnauthorizedException, AccessForbiddenException {
        var user = userServices.getUserByPersonalNumber(personalNumber);
        var course = coursesServices.getCourseEntityById(courseId);

        var hasAccess = userAccessServices.userHasAccessToCourse(user, course); // always true (throws exceptions otherwise)

        return CollectionUtils.emptyIfNull(course.getAssignments()).stream()
                .map(e -> AssignmentDTO.castBasicFromEntity(e, user))
                .collect(Collectors.toList());
    }

    /**
     * inserts or updates assignment
     *
     * @param personalNumber of user performing operation
     * @param courseId       where updated or inserted assignment is (parent)
     * @param dto            data for inserting/updating
     * @return updated object
     * @throws DataNotFoundException    course, user or assignment does not exists
     * @throws AccessForbiddenException user has no rights to perform operation
     */
    public AssignmentDTO insertOrUpdateAssignment(String personalNumber, Integer courseId, AssignmentDTO dto) throws DataNotFoundException, AccessForbiddenException {
        var user = userServices.getUserByPersonalNumber(personalNumber);
        var course = coursesServices.getCourseEntityById(courseId);

        if (!userAccessServices.userCanEditCourse(user, course))
            throw new AccessForbiddenException("Uporabnik nima dovoljenja za urejanje predmeta");


        var assignment = dto.getAssignmentId() != null
                ? assignmentRepository.findById(dto.getAssignmentId()).orElseThrow(() -> new DataNotFoundException("Ne najdem naloge")).updateFromDto(dto)
                : new AssignmentEntity(
                dto.getAssignmentId(),
                dto.getTitle(),
                dto.getClassroomUrl(),
                dto.getDescription(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getMaxSubmissionsTotal(),
                dto.getMaxSubmissionsPerStudent(),
                dto.getCoinsPerSubmission(),
                dto.getCoinsPrice(),
                dto.getInputExtension(),
                dto.getOutputExtension(),
                dto.getNotifyOnEmail(),
                dto.getPlagiarismWarning(),
                dto.getPlagiarismLevel(),
                dto.getVisible() == null || dto.getVisible(),
                dto.getArchived() != null && dto.getArchived(),
                dto.getTestType(),
                course,
                user
        );

        if (assignment.getId() != null)
            assignment.updateFromDto(dto);

        assignment = assignmentRepository.save(assignment);
        return AssignmentDTO.castFullFromEntity(
                assignment,
                (int) CollectionUtils.emptyIfNull(assignment.getSubmissions()).stream()
                        .filter(e -> e.getAuthor().equals(user)).count()
        );
    }

    /**
     * sets visibility for assignment
     *
     * @param personalNumber users personal number
     * @param assignmentId   to be updated
     * @param isVisible      update with value
     * @return updated dto object
     * @throws DataNotFoundException    user, assignment or course doesnt exists
     * @throws AccessForbiddenException user has not right to perform operation
     */
    public AssignmentDTO setVisibility(String personalNumber, Integer assignmentId, Boolean isVisible) throws DataNotFoundException, AccessForbiddenException {
        var user = userServices.getUserByPersonalNumber(personalNumber);
        var assignment = getAssignmentById(assignmentId);
        userCanEditAssignment(user, assignment);

        assignment.setVisible(isVisible);
        assignment = assignmentRepository.save(assignment);

        return AssignmentDTO.castFullFromEntity(assignment, (int) CollectionUtils.emptyIfNull(assignment.getSubmissions()).stream().filter(e -> e.getAuthor().equals(user)).count());
    }

    public AssignmentDTO getAssignmentsData(String personalNumber, Integer assignmentId) throws DataNotFoundException, AccessUnauthorizedException, AccessForbiddenException {
        var assignment = getAssignmentById(assignmentId);
        var user = userServices.getUserByPersonalNumber(personalNumber);
        userCanEditAssignment(user, assignment);

        return AssignmentDTO.castFullFromEntity(assignment, (int) CollectionUtils.emptyIfNull(assignment.getSubmissions()).stream().filter(e -> e.getAuthor().equals(user)).count());
    }

    /**
     * sets assignment as deleted in database
     *
     * @param personalNumber of user who is performing operation
     * @param assignmentId   of entity to be deleted
     * @throws DataNotFoundException    user, course or assignment doesnt exists
     * @throws AccessForbiddenException user doesnt have rights to perform operation
     */
    public void deleteAssignment(String personalNumber, Integer assignmentId) throws DataNotFoundException, AccessForbiddenException {
        var assignment = getAssignmentById(assignmentId);
        var user = userServices.getUserByPersonalNumber(personalNumber);
        userCanEditAssignment(user, assignment);

        assignmentRepository.markAsDeleted(assignment);
    }

    /**
     * sets assignment to archived status
     *
     * @param personalNumber user performing operation
     * @param assignmentId
     * @param isVisible      setting visibility
     * @return assignment
     * @throws DataNotFoundException    data not found
     * @throws AccessForbiddenException user cannot perform operation
     */
    public AssignmentDTO setArchivedStatus(String personalNumber, Integer assignmentId, Boolean isVisible) throws DataNotFoundException, AccessForbiddenException {
        var assignment = getAssignmentById(assignmentId);
        var user = userServices.getUserByPersonalNumber(personalNumber);
        userCanEditAssignment(user, assignment);

        assignment.setArchived(isVisible);
        assignment = assignmentRepository.save(assignment);

        return AssignmentDTO.castFullFromEntity(assignment, (int) CollectionUtils.emptyIfNull(assignment.getSubmissions()).stream().filter(e -> e.getAuthor().equals(user)).count());
    }

    /**
     * @param personalNumber user performing operation
     * @param assignmentId   to be returned
     * @return assignment with submissions
     * @throws DataNotFoundException    data not found
     * @throws AccessForbiddenException user cannot perform operation
     */
    public AssignmentDTO getAssignmentWithSubmissions(String personalNumber, Integer assignmentId) throws DataNotFoundException, AccessForbiddenException, AccessUnauthorizedException {
        var assignment = getAssignmentById(assignmentId);
        var user = userServices.getUserByPersonalNumber(personalNumber);
        userCanAccessAssignment(user, assignment);

        var mySubmissions =
                CollectionUtils.emptyIfNull(assignment.getSubmissions()).stream()
                        .filter(e -> e.getAuthor().equals(user));
        var boughtSubmission =
                CollectionUtils.emptyIfNull(user.getPurchases()).stream()
                        .map(PurchaseEntity::getSubmissionBought)
                        .filter(entity -> entity.getAssignment().equals(assignment));

        return AssignmentDTO.castFromEntityWithSubmissions(
                assignment,
                mySubmissions.map(SubmissionDTO::castFromEntity).collect(Collectors.toList()),
                boughtSubmission.map(SubmissionDTO::castFromEntity).collect(Collectors.toList())
        );
    }

    public List<SubmissionDTO> getAllSubmissionsOfAssignment(String personalNumber, Integer assignmentId) throws DataNotFoundException, AccessForbiddenException {
        var assignment = getAssignmentById(assignmentId);
        var user = userServices.getUserByPersonalNumber(personalNumber);
        userCanEditAssignment(user, assignment);

        return CollectionUtils.emptyIfNull(assignment.getSubmissions()).stream()
                .map(e -> SubmissionDTO.castFromEntity(e, e.getAuthor()))
                .collect(Collectors.toList());
    }

    /**
     * checks if user can edit assignment, if not exception is thrown
     *
     * @param user       to perform operation
     * @param assignment
     * @throws DataNotFoundException    data not found
     * @throws AccessForbiddenException user cannot perform operation
     */
    private void userCanEditAssignment(UserEntity user, AssignmentEntity assignment) throws DataNotFoundException, AccessForbiddenException {
        var course = assignment.getCourse();
        if (course == null)
            throw new DataNotFoundException("Ne najdem predmeta");

        if (!userAccessServices.userCanEditCourse(user, course))
            throw new AccessForbiddenException("Ni pravic");
    }

    /**
     * checks if user can access assignment, if not exception is thrown
     *
     * @param user       to perform operation
     * @param assignment
     * @throws DataNotFoundException    data not found
     * @throws AccessForbiddenException user cannot perform operation
     */
    private void userCanAccessAssignment(UserEntity user, AssignmentEntity assignment) throws DataNotFoundException, AccessForbiddenException, AccessUnauthorizedException {
        var course = assignment.getCourse();
        if (course == null)
            throw new DataNotFoundException("Ne najdem predmeta");

        if (!userAccessServices.userHasAccessToCourse(user, course))
            throw new AccessForbiddenException("Ni pravic");
    }

    public void saveSource(
            Integer assignmentId,
            String programName,
            String programLanguage,
            Double timeout,
            MultipartFile source,
            UserEntity author)
            throws DataNotFoundException, IOException {
        var assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new DataNotFoundException("Naloga s podanim IDjem ne obstaja"));

        var assignmentSource = assignment.getSource() == null
                ? new AssignmentSourceEntity(
                    assignment.getSource() != null ? assignment.getSource().getId() : null,
                    programName,
                    programLanguage,
                    source.getOriginalFilename(),
                    timeout,
                    source.getBytes(),
                    assignment,
                    author)
                : assignment.getSource().update(
                    programName,
                    programLanguage,
                    timeout,
                    source == null ? null : source.getOriginalFilename(),
                    source == null ? null : source.getBytes(),
                    author);

        assignmentSourceRepository.save(assignmentSource);
    }

    public DoubleWrapper<String, ByteArrayInputStream> downloadSource(Integer assignmentId) throws DataNotFoundException {
        var assignment = assignmentRepository.findById(assignmentId).orElseThrow(() -> new DataNotFoundException("Ne najdem isakne naloge"));
        var source = assignment.getSource();
        if (source == null)
            throw new DataNotFoundException("Ni datoteke");

        return new DoubleWrapper<>(source.getFileName(), new ByteArrayInputStream(source.getSource()));
    }
}
