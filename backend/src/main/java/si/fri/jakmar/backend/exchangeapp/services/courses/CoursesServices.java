package si.fri.jakmar.backend.exchangeapp.services.courses;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.fri.jakmar.backend.exchangeapp.database.repositories.CourseRepository;
import si.fri.jakmar.backend.exchangeapp.database.repositories.UserRepository;
import si.fri.jakmar.backend.exchangeapp.mappers.CoursesMappers;
import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.courses.CourseBasicDTO;
import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.courses.CourseDTO;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.AccessUnauthorizedException;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.DataInvalidException;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.DataNotFoundException;
import si.fri.jakmar.backend.exchangeapp.services.users.UserAccessServices;

import java.util.LinkedList;
import java.util.List;

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
        var dtos = new LinkedList<CourseBasicDTO>();

        for(var entity : coursesEntity)
            dtos.add(coursesMappers.castCourseEntityToCourseBasicDTO(entity));

        if(dtos.size() == 0)
            throw new DataNotFoundException("Ne najdem predmetov");

        return dtos;
    }

    public CourseDTO getCourseData(Integer courseId, String userPersonalNumber) throws DataNotFoundException, AccessForbiddenException, AccessUnauthorizedException {
        var users = userRepository.findUsersByPersonalNumber(userPersonalNumber);
        var courseEntityOptional = courseRepository.findById(courseId);

        if(users == null || users.size() == 0 || courseEntityOptional.isEmpty())
            throw new DataNotFoundException();

        var user = users.get(0);
        var course = courseEntityOptional.get();

        if(userAccessServices.userHasAccessToCourse(user, course))
            return coursesMappers.castCourseEntityToCourseDTO(course, user);
        else
            return null;
    }
}
