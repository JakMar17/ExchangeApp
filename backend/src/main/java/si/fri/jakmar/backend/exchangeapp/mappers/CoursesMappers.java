package si.fri.jakmar.backend.exchangeapp.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.fri.jakmar.backend.exchangeapp.database.entities.courses.CourseEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.database.repositories.CourseAccessLevelRepository;
import si.fri.jakmar.backend.exchangeapp.database.repositories.UserRepository;
import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.courses.CourseBasicDTO;
import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.courses.CourseDTO;
import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.courses.CourseDetailedDTO;
import si.fri.jakmar.backend.exchangeapp.services.users.UserAccessServices;

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
    private UserRepository userRepository;
    @Autowired
    private CourseAccessLevelRepository courseAccessLevelRepository;

    public CourseBasicDTO castCourseEntityToCourseBasicDTO(CourseEntity courseEntity) {
        return new CourseBasicDTO(
                courseEntity.getCourseId(),
                courseEntity.getCourseTitle(),
                courseEntity.getCourseDescription(),
                courseEntity.getCourseClassroomUrl(),
                usersMappers.castUserEntityToUserDTO(courseEntity.getGuardianMain(), false)
        );
    }

    public CourseDTO castCourseEntityToCourseDTO(CourseEntity courseEntity, UserEntity userEntity) {
        return new CourseDTO(
                courseEntity.getCourseId(),
                courseEntity.getCourseTitle(),
                courseEntity.getCourseDescription(),
                courseEntity.getCourseClassroomUrl(),
                usersMappers.castUserEntityToUserDTO(courseEntity.getGuardianMain(), false),
                courseEntity.getAssignments().stream()
                        .map(e -> assignmentMappers.castAssignmentEntityToAssignmentBasicDTO(e, userEntity))
                        .collect(Collectors.toList())
        );
    }

    public CourseDetailedDTO castCourseEntittyToCourseDetailedDTO(CourseEntity courseEntity, UserEntity userEntity) {
        return new CourseDetailedDTO(
                courseEntity.getCourseId(),
                courseEntity.getCourseTitle(),
                courseEntity.getCourseDescription(),
                courseEntity.getCourseClassroomUrl(),
                usersMappers.castUserEntityToUserDTO(courseEntity.getGuardianMain(), false),
                courseEntity.getAssignments().stream()
                        .map(e -> assignmentMappers.castAssignmentEntityToAssignmentBasicDTO(e, userEntity))
                        .collect(Collectors.toList()),
                userAccessServices.userCanEditCourse(userEntity, courseEntity),
                courseEntity.getUsersGuardians().stream()
                        .map(e -> usersMappers.castUserEntityToUserDTO(e, false))
                        .collect(Collectors.toList()),
                courseEntity.getUsersSignedInCourse().stream()
                        .map(e -> usersMappers.castUserEntityToUserDTO(e, false))
                        .collect(Collectors.toList()),
                courseEntity.getUsersWhitelisted().stream()
                        .map(e -> usersMappers.castUserEntityToUserDTO(e, false))
                        .collect(Collectors.toList()),
                courseEntity.getUsersBlacklisted().stream()
                        .map(e -> usersMappers.castUserEntityToUserDTO(e, false))
                        .collect(Collectors.toList()),
                courseEntity.getAccessLevel().getDescription()
        );
    }

    public CourseEntity castCourseDetailedDtoToCourseEntity(CourseDetailedDTO courseDetailedDTO) {
        return this.castCourseDetailedDtoToCourseEntity(courseDetailedDTO, new CourseEntity());
    }

    public CourseEntity castCourseDetailedDtoToCourseEntity(CourseDetailedDTO dto, CourseEntity entity) {
        entity.setUsersGuardians(usersMappers.castListOfUserDtosToUserEntities(dto.getGuardians()));
        entity.setUsersSignedInCourse(usersMappers.castListOfUserDtosToUserEntities(dto.getStudentsSignedIn()));
        entity.setUsersWhitelisted(usersMappers.castListOfUserDtosToUserEntities(dto.getStudentsWhitelisted()));
        entity.setUsersBlacklisted(usersMappers.castListOfUserDtosToUserEntities(dto.getStudentsBlacklisted()));

        var accesses = courseAccessLevelRepository.getCourseAccessLevelEntitiesByDescription(dto.getAccessType());
        if(accesses != null || accesses.size() != 0)
            entity.setAccessLevel(accesses.get(0));

        //entity.setAssignments(dto.getAssignments());
        entity.setCourseTitle(dto.getTitle());
        entity.setCourseDescription(dto.getDescription());
        entity.setCourseClassroomUrl(dto.getClassroomUrl());

        return entity;
    }

}
