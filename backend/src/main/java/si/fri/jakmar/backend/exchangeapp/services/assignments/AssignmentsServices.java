package si.fri.jakmar.backend.exchangeapp.services.assignments;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.fri.jakmar.backend.exchangeapp.database.entities.assignments.AssignmentEntity;
import si.fri.jakmar.backend.exchangeapp.database.repositories.AssignmentRepository;
import si.fri.jakmar.backend.exchangeapp.mappers.AssignmentMapper;
import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.assignments.AssignmentDTO;
import si.fri.jakmar.backend.exchangeapp.services.courses.CoursesServices;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.AccessUnauthorizedException;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.DataNotFoundException;
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
    private AssignmentMapper assignmentMapper;
    @Autowired
    private AssignmentRepository assignmentRepository;

    public AssignmentEntity getAssignmentById(Integer assignmentId) throws DataNotFoundException {
        var optional = assignmentRepository.findById(assignmentId);
        if (optional.isEmpty())
            throw new DataNotFoundException("Naloga s podanim ID-jem ne obstaja");
        else
            return optional.get();
    }

    public List<AssignmentDTO> getBasicDataForAssignmentsOfCourse(String personalNumber, Integer courseId) throws DataNotFoundException, AccessUnauthorizedException, AccessForbiddenException {
        var user = userServices.getUserByPersonalNumber(personalNumber);
        var course = coursesServices.getCourseEntityById(courseId);

        var hasAccess = userAccessServices.userHasAccessToCourse(user, course); // always true (throws exceptions otherwise)

        return CollectionUtils.emptyIfNull(course.getAssignments()).stream()
                .map(e -> assignmentMapper.castAssignmentEntityToAssignmentBasicDTO(e, user))
                .collect(Collectors.toList());
    }

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
                null,//assignmentDTO.getTestType()
                null,
                course,
                user
        );

        assignmentEntity = assignmentRepository.save(assignmentEntity);
        return assignmentMapper.castAssignmentEntityToAssignmentDTO(assignmentEntity, user);
    }

    public AssignmentDTO setVisibility(String personalNumber, Integer assignmentId, Boolean isVisible) throws DataNotFoundException, AccessForbiddenException {
        var user = userServices.getUserByPersonalNumber(personalNumber);
        var assignment = getAssignmentById(assignmentId);
        var course = assignment.getCourse();

        if(course == null)
            throw new DataNotFoundException("Ne najdem predmeta");

        if(!userAccessServices.userCanEditCourse(user, course))
            throw new AccessForbiddenException("Uporabnik nima pravice za urejanje predmeta");

        assignment.setVisible(isVisible ? 1 :0);
        assignment = assignmentRepository.save(assignment);

        return assignmentMapper.castAssignmentEntityToAssignmentDTO(assignment, user);
    }
}
