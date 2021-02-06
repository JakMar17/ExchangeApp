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
import java.util.Optional;
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

    /**
     * returns basic data of all courses in system
     * TODO: deleted/archived
     *
     * @return list of courses with basic data
     * @throws DataNotFoundException no courses found
     */
    public List<CourseBasicDTO> getAllCoursesWithBasicInfo() throws DataNotFoundException {
        var coursesEntity = courseRepository.findAll();
        var dtos =
                StreamSupport.stream(coursesEntity.spliterator(), true)
                        .map(e -> coursesMappers.castCourseEntityToCourseBasicDTO(e)).collect(Collectors.toList());

        if (dtos.size() == 0)
            throw new DataNotFoundException("Ne najdem predmetov");

        return dtos;
    }


    /**
     * return course data if user has right to view course
     *
     * @param courseId           course
     * @param userPersonalNumber user's personal number
     * @return course data if user can access it
     * @throws DataNotFoundException       either user or course cannot be found
     * @throws AccessForbiddenException    user does not have right to access (is either blacklisted or not whitelisted)
     * @throws AccessUnauthorizedException user does not have right to access (is not signed in yet and course has password protection)
     */
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

    /**
     * get course basic data
     *
     * @param courseId course ID
     * @return course's basic data
     */
    public CourseBasicDTO getCourseBasicData(Integer courseId) {
        var courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isEmpty())
            return null;

        var courseEntity = courseOptional.get();
        var dto = coursesMappers.castCourseEntityToCourseBasicDTO(courseEntity);
        return dto;
    }

    /**
     * checks given password and if it is OK signs user in course
     *
     * @param courseId           course ID
     * @param userPersonalNumber user's personal number
     * @param password           inputed password
     * @return course data if password OK
     * @throws DataNotFoundException       course with given ID does not exists
     * @throws AccessForbiddenException    user does not have right to access (is either blacklisted or not whitelisted)
     * @throws AccessUnauthorizedException user does not have right to access (is not signed in yet and course has password protection)
     */
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

    /**
     * get all course data (for editing) only guardians and admins has all access
     *
     * @param courseId           course ID
     * @param userPersonalNumber user's personal number
     * @return course data
     * @throws DataNotFoundException    course with given ID or person with given number does not exists
     * @throws AccessForbiddenException given user does not have access right for data
     */
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

    /**
     * updates or inserts course with given data
     *
     * @param courseId           course ID
     * @param userPersonalNumber user's personal number
     * @throws DataNotFoundException    course with given ID or person with given number does not exists
     * @throws AccessForbiddenException given user does not have access right for data
     */
    public void updateCourse(Integer courseId, String userPersonalNumber, CourseDetailedDTO courseDTO) throws DataNotFoundException, AccessForbiddenException {
        var users = userRepository.findUsersByPersonalNumber(userPersonalNumber);
        var cOptional = courseId != null ? courseRepository.findById(courseId) : Optional.<CourseEntity>empty();
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
