package si.fri.jakmar.backend.exchangeapp.mappers.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.courses.CourseBasicDTO;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.database.repositories.UserRepository;
import si.fri.jakmar.backend.exchangeapp.mappers.CoursesMappers;
import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.users.UserDTO;

import java.util.ArrayList;

@Component
public class UsersMappers {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CoursesMappers coursesMappers;

    public UserDTO castUserEntityToUserDTO(UserEntity userEntity, boolean withCourses) {
        var courses = new ArrayList<CourseBasicDTO>();

        if(withCourses) {
            for (var courseEntity : userEntity.getUsersCourses()) {
                courseEntity.getGuardianMain().setUsersCourses(null);
                courses.add(coursesMappers.castCourseEntityToCourseBasicDTO(courseEntity));
            }

            for (var course : userEntity.getGuardinasCourses()) {
                course.getGuardianMain().setUsersCourses(null);
                courses.add(coursesMappers.castCourseEntityToCourseBasicDTO(course));
            }

            for (var course : userEntity.getCreatedCourses()) {
                course.getGuardianMain().setUsersCourses(null);
                courses.add(coursesMappers.castCourseEntityToCourseBasicDTO(course));
            }
        }

        return new UserDTO(
                userEntity.getEmail(),
                userEntity.getName(),
                userEntity.getSurname(),
                userEntity.getPersonalNumber(),
                userEntity.getUserType().getDescription(),
                courses
        );
    }
}
