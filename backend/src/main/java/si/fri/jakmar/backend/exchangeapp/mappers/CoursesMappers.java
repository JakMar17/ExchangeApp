package si.fri.jakmar.backend.exchangeapp.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.assignments.AssignmentBasicDTO;
import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.courses.CourseBasicDTO;
import si.fri.jakmar.backend.exchangeapp.database.entities.courses.CourseEntity;
import si.fri.jakmar.backend.exchangeapp.mappers.users.UsersMappers;
import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.courses.CourseDTO;

import java.util.LinkedList;

@Component
public class CoursesMappers {

    @Autowired
    private UsersMappers usersMappers;
    @Autowired
    private AssignmentMapper assignmentMappers;

    public CourseBasicDTO castCourseEntityToCourseBasicDTO(CourseEntity courseEntity) {
        return new CourseBasicDTO(
                courseEntity.getCourseId(),
                courseEntity.getCourseTitle(),
                courseEntity.getCourseDescription(),
                courseEntity.getCourseClassroomUrl(),
                usersMappers.castUserEntityToUserDTO(courseEntity.getGuardianMain())
        );
    }

    public CourseDTO castCourseEntityToCourseDTO(CourseEntity courseEntity, UserEntity userEntity) {
        var assignments = new LinkedList<AssignmentBasicDTO>();
        for(var a : courseEntity.getAssignments())
            assignments.add(assignmentMappers.castAssignmentEntityToAssignmentBasicDTO(a, userEntity));

        return new CourseDTO(
                courseEntity.getCourseId(),
                courseEntity.getCourseTitle(),
                courseEntity.getCourseDescription(),
                courseEntity.getCourseClassroomUrl(),
                usersMappers.castUserEntityToUserDTO(courseEntity.getGuardianMain()),
                assignments
        );
    }
}
