package si.fri.jakmar.backend.exchangeapp.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.fri.jakmar.backend.exchangeapp.database.entities.courses.CourseEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.dtos.courses.CourseDTO;
import si.fri.jakmar.backend.exchangeapp.services.users.UserAccessServices;
import  org.apache.commons.collections4.CollectionUtils;
import si.fri.jakmar.backend.exchangeapp.services.users.UserServices;

import java.util.stream.Collectors;

@Component
public class CoursesMappers {

    @Autowired
    private UsersMappers usersMappers;
    @Autowired
    private AssignmentMapper assignmentMappers;
    @Autowired
    private UserAccessServices userAccessServices;
    @Autowired
    private UserServices userServices;

    public CourseDTO castCourseEntityToCourseBasicDTO(CourseEntity courseEntity) {
        return new CourseDTO(
                courseEntity.getCourseId(),
                courseEntity.getCourseTitle(),
                courseEntity.getCourseDescription(),
                courseEntity.getCourseClassroomUrl(),
                usersMappers.castUserEntityToUserDTO(courseEntity.getGuardianMain(), false),
                courseEntity.getAccessLevel().getDescription()
        );
    }

    public CourseDTO castCourseEntityToCourseDTO(CourseEntity courseEntity, UserEntity userEntity) {
        return new CourseDTO(
                courseEntity.getCourseId(),
                courseEntity.getCourseTitle(),
                courseEntity.getCourseDescription(),
                courseEntity.getCourseClassroomUrl(),
                usersMappers.castUserEntityToUserDTO(courseEntity.getGuardianMain(), false),
                courseEntity.getAccessLevel().getDescription(),
                CollectionUtils.emptyIfNull(courseEntity.getAssignments()).stream()
                        .map(e -> assignmentMappers.castAssignmentEntityToAssignmentBasicDTO(e, userEntity))
                        .collect(Collectors.toList()),
                userServices.getUsersCoinsInCourse(userEntity, courseEntity)
        );
    }

    public CourseDTO castCourseEntityToCourseDetailedDTO(CourseEntity courseEntity, UserEntity userEntity) {
        return new CourseDTO(
                courseEntity.getCourseId(),
                courseEntity.getCourseTitle(),
                courseEntity.getCourseDescription(),
                courseEntity.getCourseClassroomUrl(),
                usersMappers.castUserEntityToUserDTO(courseEntity.getGuardianMain(), false),
                courseEntity.getAccessLevel().getDescription(),
                CollectionUtils.emptyIfNull(courseEntity.getAssignments()).stream()
                        .map(e -> assignmentMappers.castAssignmentEntityToAssignmentBasicDTO(e, userEntity))
                        .collect(Collectors.toList()),
                userAccessServices.userCanEditCourse(userEntity, courseEntity),
                CollectionUtils.emptyIfNull(courseEntity.getUsersGuardians()).stream()
                        .map(e -> usersMappers.castUserEntityToUserDTO(e, false))
                        .collect(Collectors.toList()),
                CollectionUtils.emptyIfNull(courseEntity.getUsersSignedInCourse()).stream()
                        .map(e -> usersMappers.castUserEntityToUserDTO(e, false))
                        .collect(Collectors.toList()),
                CollectionUtils.emptyIfNull(courseEntity.getUsersWhitelisted()).stream()
                        .map(e -> usersMappers.castUserEntityToUserDTO(e, false))
                        .collect(Collectors.toList()),
                CollectionUtils.emptyIfNull(courseEntity.getUsersBlacklisted()).stream()
                        .map(e -> usersMappers.castUserEntityToUserDTO(e, false))
                        .collect(Collectors.toList()),
                courseEntity.getAccessLevel().getDescription(),
                courseEntity.getInitialCoins(),
                courseEntity.getAccessPassword() == null ? null : courseEntity.getAccessPassword().getPassword(),
                userServices.getUsersCoinsInCourse(userEntity, courseEntity)
        );
    }

}
