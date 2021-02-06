package si.fri.jakmar.backend.exchangeapp.services.courses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.fri.jakmar.backend.exchangeapp.database.entities.courses.CourseEntity;
import si.fri.jakmar.backend.exchangeapp.database.repositories.CourseRepository;
import si.fri.jakmar.backend.exchangeapp.database.repositories.UserRepository;
import si.fri.jakmar.backend.exchangeapp.mappers.CoursesMappers;
import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.courses.CourseBasicDTO;
import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.courses.CourseDTO;
import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.courses.CourseDetailedDTO;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.AccessUnauthorizedException;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.DataNotFoundException;
import si.fri.jakmar.backend.exchangeapp.services.users.UserAccessServices;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class CoursesServices {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CoursesMappers coursesMappers;
    @Autowired
    private UserAccessServices userAccessServices;

    public List<CourseBasicDTO> getAllCoursesWithBasicInfo() throws DataNotFoundException {
        var coursesEntity = courseRepository.findAll();
        var dtos =
                StreamSupport.stream(coursesEntity.spliterator(), true)
                        .map(e -> coursesMappers.castCourseEntityToCourseBasicDTO(e)).collect(Collectors.toList());

        if (dtos.size() == 0)
            throw new DataNotFoundException("Ne najdem predmetov");

        return dtos;
    }

    public CourseDTO getCourseData(Integer courseId, String userPersonalNumber) throws DataNotFoundException, AccessForbiddenException, AccessUnauthorizedException {
        var users = userRepository.findUsersByPersonalNumber(userPersonalNumber);
        var courseEntityOptional = courseRepository.findById(courseId);

        if (users == null || users.size() == 0 || courseEntityOptional.isEmpty())
            throw new DataNotFoundException();

        var user = users.get(0);
        var course = courseEntityOptional.get();

        if (userAccessServices.userHasAccessToCourse(user, course)) {
            var courseDto = coursesMappers.castCourseEntityToCourseDTO(course, user);
            courseDto.setUserHasAdminRights(userAccessServices.userCanEditCourse(user, course));
            return courseDto;
        } else
            return null;
    }

    public CourseBasicDTO getCourseBasicData(Integer courseId) {
        var courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isEmpty())
            return null;

        var courseEntity = courseOptional.get();
        var dto = coursesMappers.castCourseEntityToCourseBasicDTO(courseEntity);
        return dto;
    }

    public CourseDTO checkPasswordAndGetCourse(Integer courseId, String userPersonalNumber, String password) throws DataNotFoundException, AccessUnauthorizedException, AccessForbiddenException {
        var courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isEmpty())
            throw new DataNotFoundException();

        var course = courseOptional.get();
        if (course.getAccessPassword().getPassword().equals(password)) {
            userAccessServices.signUserInCourse(userRepository.findUsersByPersonalNumber(userPersonalNumber).get(0), course);
            return this.getCourseData(courseId, userPersonalNumber);
        } else {
            throw new AccessUnauthorizedException("Geslo ni pravilno");
        }
    }

    public CourseDetailedDTO getCourseDetailed(Integer courseId, String userPersonalNumber) throws DataNotFoundException, AccessForbiddenException {
        var cOptional = courseRepository.findById(courseId);
        var users = userRepository.findUsersByPersonalNumber(userPersonalNumber);
        if (cOptional.isEmpty() || users.size() == 0)
            throw new DataNotFoundException();

        var course = cOptional.get();
        var user = users.get(0);
        if (userAccessServices.userCanEditCourse(user, course))
            return coursesMappers.castCourseEntittyToCourseDetailedDTO(course, user);
        else
            throw new AccessForbiddenException("Uporabnik nima pravice za dostop do podatkov");
    }

    public void updateCourse(Integer courseId, String userPersonalNumber, CourseDetailedDTO courseDTO) throws DataNotFoundException, AccessForbiddenException {
        var users = userRepository.findUsersByPersonalNumber(userPersonalNumber);
        var cOptional = courseId != null ? courseRepository.findById(courseId) : null;
        if (users.size() == 0 || (courseId != null && cOptional.isEmpty()))
            throw new DataNotFoundException();

        if (courseId == null || userAccessServices.userCanEditCourse(users.get(0), cOptional.get())) {
            var entity = coursesMappers.castCourseDetailedDtoToCourseEntity(courseDTO, cOptional.orElse(new CourseEntity()));
            courseRepository.save(entity);
        } else {
            throw new AccessForbiddenException("Uporabnik nima pravice za dostop do podatkov");
        }
    }
}
