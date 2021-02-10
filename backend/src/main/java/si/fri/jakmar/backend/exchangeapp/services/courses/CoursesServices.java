package si.fri.jakmar.backend.exchangeapp.services.courses;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.fri.jakmar.backend.exchangeapp.database.entities.courses.CourseAccessLevelEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.courses.CourseAccessPassword;
import si.fri.jakmar.backend.exchangeapp.database.entities.courses.CourseEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.database.repositories.course.CourseAccessLevelRepository;
import si.fri.jakmar.backend.exchangeapp.database.repositories.course.CourseAccessPasswordRepository;
import si.fri.jakmar.backend.exchangeapp.database.repositories.course.CourseRepository;
import si.fri.jakmar.backend.exchangeapp.dtos.courses.CourseDTO;
import si.fri.jakmar.backend.exchangeapp.mappers.CoursesMappers;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.AccessUnauthorizedException;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.DataNotFoundException;
import si.fri.jakmar.backend.exchangeapp.services.users.UserAccessServices;
import si.fri.jakmar.backend.exchangeapp.services.users.UserServices;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class CoursesServices {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseAccessPasswordRepository courseAccessPasswordRepository;
    @Autowired
    private CourseAccessLevelRepository courseAccessLevelRepository;

    @Autowired
    private UserServices userServices;
    @Autowired
    private CoursesMappers coursesMappers;
    @Autowired
    private UserAccessServices userAccessServices;

    /**
     * gets CourseEntity from database with given courseId
     *
     * @param courseId attribute for finding entity
     * @return course with given id
     * @throws DataNotFoundException course with given id doesnt exists
     */
    public CourseEntity getCourseEntityById(Integer courseId) throws DataNotFoundException {
        var o = courseRepository.findById(courseId);
        if (o.isEmpty())
            throw new DataNotFoundException("Iskan predmet ne obstaja");
        else
            return o.get();
    }

    /**
     * returns basic data of all courses in system
     * TODO: deleted/archived
     *
     * @return list of courses with basic data
     * @throws DataNotFoundException no courses found
     */
    public List<CourseDTO> getAllCoursesWithBasicInfo() throws DataNotFoundException {
        var coursesEntity = courseRepository.findAll();
        var dtos =
                StreamSupport.stream(coursesEntity.spliterator(), true)
                        .map(CourseDTO::castBasicFromEntity).collect(Collectors.toList());

        if (dtos.size() == 0)
            throw new DataNotFoundException("Ne najdem predmetov");

        return dtos;
    }

    public List<CourseDTO> getAllCoursesOfUserWithBasicInfo(String personalNumber) throws DataNotFoundException {
        UserEntity user = userServices.getUserByPersonalNumber(personalNumber);
        return CollectionUtils.emptyIfNull(user.getUsersCourses()).stream()
                .map(CourseDTO::castBasicFromEntity)
                .collect(Collectors.toList());
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
        UserEntity user = userServices.getUserByPersonalNumber(userPersonalNumber);
        var courseEntityOptional = courseRepository.findById(courseId);

        if (courseEntityOptional.isEmpty())
            throw new DataNotFoundException();

        var course = courseEntityOptional.get();

        if (userAccessServices.userHasAccessToCourse(user, course)) {
            var courseDto = CourseDTO.castFromEntity(course, user, userServices.getUsersCoinsInCourse(user, course));
            courseDto.setUserCanEditCourse(userAccessServices.userCanEditCourse(user, course));
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
    public CourseDTO getCourseBasicData(Integer courseId) {
        var courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isEmpty())
            return null;

        var courseEntity = courseOptional.get();
        var dto = CourseDTO.castBasicFromEntity(courseEntity);
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
            userAccessServices.signUserInCourse(userServices.getUserByPersonalNumber(userPersonalNumber), course);
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
    public CourseDTO getCourseDetailed(Integer courseId, String userPersonalNumber) throws DataNotFoundException, AccessForbiddenException {
        var cOptional = courseRepository.findById(courseId);
        UserEntity user = userServices.getUserByPersonalNumber(userPersonalNumber);
        if (cOptional.isEmpty())
            throw new DataNotFoundException();

        var course = cOptional.get();
        if (userAccessServices.userCanEditCourse(user, course))
            return CourseDTO.castFullFromEntity(course, user, userAccessServices.userCanEditCourse(user, course), userServices.getUsersCoinsInCourse(user, course));
        else
            throw new AccessForbiddenException("Uporabnik nima pravice za dostop do podatkov");
    }


    /**
     * insert new course or update existing if course id is given
     *
     * @param personalNumber personal number of user who is updating/inserting
     * @param courseDto      data for update/insert
     * @return updated courseDto object
     * @throws DataNotFoundException    either user or course dont exists
     * @throws AccessForbiddenException user does not have rights for operation
     */
    public CourseDTO insertOrUpdateCourse(String personalNumber, CourseDTO courseDto) throws DataNotFoundException, AccessForbiddenException {
        boolean insertNew = courseDto.getCourseId() == null;
        UserEntity user = userServices.getUserByPersonalNumber(personalNumber);
        CourseEntity course;

        if (insertNew) {
            course = new CourseEntity();
        } else {
            var courseOptional = courseRepository.findById(courseDto.getCourseId());
            if (courseOptional.isEmpty())
                throw new DataNotFoundException("Predmet s podanim id-jem ne obstaja");
            else
                course = courseOptional.get();

        }

        if (!userAccessServices.userCanEditCourse(user, course))
            throw new AccessForbiddenException("Uporabnik nima dovoljenja za urejanje predmeta");

        course = course.courseUpdater(
                courseDto.getTitle(),
                courseDto.getDescription(),
                courseDto.getClassroomURL(),
                courseDto.getInitialCoins(),
                getCourseAccessLevelEntity(courseDto.getAccessLevel()),
                createPasswordForCourse(courseDto.getAccessPassword()),
                user,
                null, //signed in
                courseDto.getStudentsBlacklisted() != null
                        ? courseDto.getStudentsBlacklisted().stream()
                        .map(e -> getUserEntityWithoutExceptions(e.getPersonalNumber(), e.getEmail()))
                        .collect(Collectors.toList())
                        : null,
                courseDto.getStudentsWhitelisted() != null
                        ? courseDto.getStudentsWhitelisted().stream()
                        .map(e -> getUserEntityWithoutExceptions(e.getPersonalNumber(), e.getEmail()))
                        .collect(Collectors.toList())
                        : null,
                courseDto.getGuardians() != null
                        ? courseDto.getGuardians().stream()
                        .map(e -> getUserEntityWithoutExceptions(e.getPersonalNumber(), e.getEmail()))
                        .collect(Collectors.toList())
                        : null,
                false
        );

        var courseEntity = courseRepository.save(course);
        return CourseDTO.castFullFromEntity(courseEntity, user, userAccessServices.userCanEditCourse(user, course), userServices.getUsersCoinsInCourse(user, course));
    }

    /**
     * gets user entity by either personal number or email, without throwing errors
     *
     * @param personalNumber users personal number
     * @param email          users email
     * @return UserEntity if user exists or null
     */
    private UserEntity getUserEntityWithoutExceptions(String personalNumber, String email) {
        try {
            return userServices.getUserByEmailOrPersonalNumber(personalNumber, email);
        } catch (DataNotFoundException dataNotFoundException) {
            dataNotFoundException.printStackTrace();
        }
        return null;
    }

    /**
     * creates course's password and return entity
     *
     * @param password password to be created
     * @return CourseAccessPassword with given password
     */
    private CourseAccessPassword createPasswordForCourse(String password) {
        if (password == null)
            return null;
        else
            return courseAccessPasswordRepository.save(new CourseAccessPassword(password));
    }

    /**
     * gets course's access level by given description
     *
     * @param str description of access level
     * @return CourseAccessLevelEntity with given description
     * @throws DataNotFoundException entity with given description doesn't exists
     */
    private CourseAccessLevelEntity getCourseAccessLevelEntity(String str) throws DataNotFoundException {
        var list = courseAccessLevelRepository.getCourseAccessLevelEntitiesByDescription(str);
        if (list == null || list.isEmpty())
            throw new DataNotFoundException("Ne najdem varnostnega mehanizma");
        else
            return list.get(0);
    }
}
