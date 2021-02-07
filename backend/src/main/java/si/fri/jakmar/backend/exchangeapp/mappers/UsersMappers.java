package si.fri.jakmar.backend.exchangeapp.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.fri.jakmar.backend.exchangeapp.database.repositories.UserRepository;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.courses.CourseDTO;
import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.users.UserDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsersMappers {

    @Autowired private CoursesMappers coursesMappers;
    @Autowired private UserRepository userRepository;

    public UserDTO castUserEntityToUserDTO(UserEntity userEntity, boolean withCourses) {
        ArrayList<CourseDTO> coursesDtos = new ArrayList<>();

        if (withCourses) {
            var coursesEntities = userEntity.getUsersCourses();
            coursesEntities.addAll(userEntity.getCreatedCourses().stream().filter(f -> !coursesEntities.contains(f)).collect(Collectors.toList()));
            coursesEntities.addAll(userEntity.getGuardinasCourses().stream().filter(f -> !coursesEntities.contains(f)).collect(Collectors.toList()));

            coursesDtos.addAll(coursesEntities.stream().map(e -> coursesMappers.castCourseEntityToCourseBasicDTO(e)).collect(Collectors.toList()));
        }

        return new UserDTO(
                userEntity.getEmail(),
                userEntity.getName(),
                userEntity.getSurname(),
                userEntity.getPersonalNumber(),
                userEntity.getUserType().getDescription(),
                coursesDtos
        );
    }

    public UserEntity castUserDtoToUserEntity(UserDTO dto) {
        return this.castUserDtoToUserEntity(dto, new UserEntity());
    }

    public UserEntity castUserDtoToUserEntity(UserDTO dto, UserEntity entity) {
        entity.setEmail(dto.getEmail());
        entity.setName(dto.getName());
        entity.setName(dto.getSurname());
        entity.setPersonalNumber(dto.getPersonalNumber());
        //usertype
        //courses

        return entity;
    }

    public List<UserEntity> castListOfUserDtosToUserEntities(List<UserDTO> dtos) {
        return dtos.stream()
                .map(e ->
                        castUserDtoToUserEntity(
                                e,
                                userRepository.findUsersByPersonalNumber(e.getPersonalNumber()).get(0)
                        )
                )
                .collect(Collectors.toList());
    }
}
