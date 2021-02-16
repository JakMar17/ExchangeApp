package si.fri.jakmar.backend.exchangeapp.services.assignments;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.fri.jakmar.backend.exchangeapp.database.entities.assignments.AssignmentEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.assignments.SubmissionCheckEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.purchases.PurchaseEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.database.repositories.AssignmentRepository;
import si.fri.jakmar.backend.exchangeapp.dtos.assignments.AssignmentDTO;
import si.fri.jakmar.backend.exchangeapp.dtos.submissions.SubmissionDTO;
import si.fri.jakmar.backend.exchangeapp.services.courses.CoursesServices;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessUnauthorizedException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.DataNotFoundException;
import si.fri.jakmar.backend.exchangeapp.services.users.UserAccessServices;
import si.fri.jakmar.backend.exchangeapp.services.users.UserServices;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AssignmentsServices {
    @Autowired
    private UserServices userServices;
    @Autowired
    private UserAccessServices userAccessServices;
    @Autowired
    private CoursesServices coursesServices;
    @Autowired
    private AssignmentRepository assignmentRepository;

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
     * @param assignmentDTO  data for inserting/updating
     * @return updated object
     * @throws DataNotFoundException    course, user or assignment does not exists
     * @throws AccessForbiddenException user has no rights to perform operation
     */
    public AssignmentDTO insertOrUpdateAssignment(String personalNumber, Integer courseId, AssignmentDTO assignmentDTO) throws DataNotFoundException, AccessForbiddenException {
        boolean insertNew = assignmentDTO.getAssignmentId() == null;
        var user = userServices.getUserByPersonalNumber(personalNumber);
        var course = coursesServices.getCourseEntityById(courseId);

        AssignmentEntity assignmentEntity;
        if (insertNew) {
            assignmentEntity = new AssignmentEntity();
        } else {
            assignmentEntity = getAssignmentById(assignmentDTO.getAssignmentId());
        }

        if (!userAccessServices.userCanEditCourse(user, course))
            throw new AccessForbiddenException("Uporabnik nima dovoljenja za urejanje predmeta");

        assignmentEntity.assignmentUpdater(
                assignmentDTO.getTitle(),
                assignmentDTO.getClassroomUrl(),
                assignmentDTO.getDescription(),
                assignmentDTO.getStartDate(),
                assignmentDTO.getEndDate(),
                assignmentDTO.getMaxSubmissionsTotal(),
                assignmentDTO.getMaxSubmissionsPerStudent(),
                assignmentDTO.getCoinsPerSubmission(),
                assignmentDTO.getCoinsPrice(),
                assignmentDTO.getInputExtension(),
                assignmentDTO.getOutputExtension(),
                assignmentDTO.getNotifyOnEmail() ? 1 : 0,
                assignmentDTO.getPlagiarismWarning(),
                assignmentDTO.getPlagiarismLevel(),
                assignmentDTO.getVisible() ? 1 : 0,
                new SubmissionCheckEntity(1),//assignmentDTO.getTestType()
                null,
                course,
                user,
                false
        );

        assignmentEntity = assignmentRepository.save(assignmentEntity);
        return AssignmentDTO.castFullFromEntity(assignmentEntity, (int) CollectionUtils.emptyIfNull(assignmentEntity.getSubmissions()).stream().filter(e -> e.getAuthor().equals(user)).count());
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
        userCanAccessAssignment(user, assignment);

        assignment.setVisible(isVisible ? 1 : 0);
        assignment = assignmentRepository.save(assignment);

        return AssignmentDTO.castFullFromEntity(assignment, (int) CollectionUtils.emptyIfNull(assignment.getSubmissions()).stream().filter(e -> e.getAuthor().equals(user)).count());
    }

    public AssignmentDTO getAssignmentsData(String personalNumber, Integer assignmentId) throws DataNotFoundException, AccessUnauthorizedException, AccessForbiddenException {
        var assignment = getAssignmentById(assignmentId);
        var user = userServices.getUserByPersonalNumber(personalNumber);
        userCanAccessAssignment(user, assignment);

        return AssignmentDTO.castFullFromEntity(assignment, (int) CollectionUtils.emptyIfNull(assignment.getSubmissions()).stream().filter(e -> e.getAuthor().equals(user)).count());
    }

    /**
     * delets entity from database
     *
     * @param personalNumber of user who is performing operation
     * @param assignmentId   of entity to be deleted
     * @throws DataNotFoundException    user, course or assignment doesnt exists
     * @throws AccessForbiddenException user doesnt have rights to perform operation
     */
    public void deleteAssignment(String personalNumber, Integer assignmentId) throws DataNotFoundException, AccessForbiddenException {
        var assignment = getAssignmentById(assignmentId);
        var user = userServices.getUserByPersonalNumber(personalNumber);
        userCanAccessAssignment(user, assignment);

        assignmentRepository.delete(assignment);
    }

    /**
     * sets assignment to archived status
     * @param personalNumber user performing operation
     * @param assignmentId
     * @param isVisible setting visibility
     * @return assignment
     * @throws DataNotFoundException data not found
     * @throws AccessForbiddenException user cannot perform operation
     */
    public AssignmentDTO setArchivedStatus(String personalNumber, Integer assignmentId, Boolean isVisible) throws DataNotFoundException, AccessForbiddenException {
        var assignment = getAssignmentById(assignmentId);
        var user = userServices.getUserByPersonalNumber(personalNumber);
        userCanAccessAssignment(user, assignment);

        assignment.setArchived(isVisible);
        assignment = assignmentRepository.save(assignment);

        return AssignmentDTO.castFullFromEntity(assignment, (int) CollectionUtils.emptyIfNull(assignment.getSubmissions()).stream().filter(e -> e.getAuthor().equals(user)).count());
    }

    /**
     * @param personalNumber user performing operation
     * @param assignmentId to be returned
     * @return assignment with submissions
     * @throws DataNotFoundException data not found
     * @throws AccessForbiddenException user cannot perform operation
     */
    public AssignmentDTO getAssignmentWithSubmissions(String personalNumber, Integer assignmentId) throws DataNotFoundException, AccessForbiddenException {
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

    /**
     * checks if user can access assignment, if not exception is thrown
     * @param user to perform operation
     * @param assignment
     * @throws DataNotFoundException data not found
     * @throws AccessForbiddenException user cannot perform operation
     */
    private void userCanAccessAssignment(UserEntity user, AssignmentEntity assignment) throws DataNotFoundException, AccessForbiddenException {
        var course = assignment.getCourse();
        if (course == null)
            throw new DataNotFoundException("Ne najdem predmeta");

        if (!userAccessServices.userCanEditCourse(user, course))
            throw new AccessForbiddenException("Ni pravic");
    }
}
