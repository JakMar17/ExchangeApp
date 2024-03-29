package si.fri.jakmar.backend.exchangeapp.dtos.users;

import org.apache.commons.collections4.CollectionUtils;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.dtos.courses.CourseDTO;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserDTO {
    private String email;
    private String name;
    private String surname;
    private String personalNumber;
    private String userType;
    private List<CourseDTO> myCourses;

    public UserDTO() {
    }

    public UserDTO(String email, String name, String surname, String personalNumber, String userType, List<CourseDTO> myCourses) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.personalNumber = personalNumber;
        this.userType = userType;
        this.myCourses = myCourses;
    }

    public static UserDTO castFromEntityWithoutCourses(UserEntity entity) {
        return castFromEntityWithoutCourses(entity, false);
    }

    public static UserDTO castFromEntityWithoutCourses(UserEntity entity, boolean withUserType) {
        return new UserDTO(
                entity.getUsername(),
                entity.getName(),
                entity.getSurname(),
                entity.getPersonalNumber(),
                withUserType
                        ? entity.getUserType().name()
                        : null,
                null
        );
    }

    public static UserDTO castFromEntity(UserEntity entity, boolean withUserType, boolean withCourses) {
        return new UserDTO(
                entity.getUsername(),
                entity.getName(),
                entity.getSurname(),
                entity.getPersonalNumber(),
                withUserType
                        ? entity.getUserType().name()
                        : null,
                withCourses
                        ? Stream.concat(
                        CollectionUtils.emptyIfNull(entity.getUsersCourses()).stream(),
                        Stream.concat(
                                CollectionUtils.emptyIfNull(entity.getCreatedCourses()).stream(),
                                CollectionUtils.emptyIfNull(entity.getGuardiansCourses()).stream())
                        )
                        .distinct()
                        .map(CourseDTO::castBasicFromEntity)
                        .collect(Collectors.toList())
                        : null
        );
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<CourseDTO> getMyCourses() {
        return myCourses;
    }

    public void setMyCourses(List<CourseDTO> myCourses) {
        this.myCourses = myCourses;
    }
}
