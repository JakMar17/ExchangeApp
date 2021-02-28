package si.fri.jakmar.backend.exchangeapp.services.users;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.fri.jakmar.backend.exchangeapp.database.entities.courses.CourseEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.database.repositories.course.CourseRepository;
import si.fri.jakmar.backend.exchangeapp.database.repositories.user.UserRepository;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessUnauthorizedException;

import java.util.List;

@Component
public class UserAccessServices {

    @Autowired
    private UserRepository userRepository;
    @Autowired private CourseRepository courseRepository;

    /**
     * checks if given userEntity has access to given courseEntity
     * has access = is admin OR is main guardian OR is not on blacklist and is either on whitelist, signed in, guardian or is course public
     * @param userEntity user to be check
     * @param courseEntity course to be checked
     * @return true if user has access
     * @throws AccessForbiddenException user is either blacklisted or not whitelisted (if course has whitelist)
     * @throws AccessUnauthorizedException user is not signed in course yet, course requires password access
     */
    public boolean userHasAccessToCourse(UserEntity userEntity, CourseEntity courseEntity) throws AccessForbiddenException, AccessUnauthorizedException {
        boolean hasAccess = false;

        if(userIsAdmin((userEntity)))
            hasAccess = true;

        if(userIsGuardian(userEntity, courseEntity))
            hasAccess = true;

        //check if on blacklist
        if(!hasAccess && userOnList(userEntity, courseEntity.getUsersBlacklisted()))
            throw new AccessForbiddenException("Uporabniku je blokiran dostop do predmeta");

        if(!hasAccess)
            switch (courseEntity.getAccessLevel().getDescription()) {
                case "WHITELIST":
                    if(userOnList(userEntity, courseEntity.getUsersWhitelisted()))
                        hasAccess = true;
                    else
                        throw new AccessForbiddenException("Uporabnik ni dodan med izjeme, ki imajo dostop do predmeta");
                    break;
                case "PASSWORD":
                    if(userOnList(userEntity, courseEntity.getUsersSignedInCourse()))
                        hasAccess = true;
                    else
                        throw new AccessUnauthorizedException("Predmet je zaščiten z geslom, vnesite ga preden lahko nadaljujete");
                    break;
                default:
                    hasAccess = true;
            }


        signUserInCourse(userEntity, courseEntity);
        return true;
    }

    /**
     * checks if user has admin role
     * @param userEntity user to be checked
     * @return true if user has admin rights
     */
    public boolean userIsAdmin(UserEntity userEntity) {
        return userEntity.getUserType().getId() == 1;
    }

    public boolean userIsProfessor(UserEntity userEntity) {
        return userEntity.getUserType().getId() == 2;
    }

    /**
     * checks if user is guardian on course
     * @param userEntity user to be checked
     * @param courseEntity course to be checked
     * @return true if user is guardian on course, otherwise returns false
     */
    private boolean userIsGuardian(UserEntity userEntity, CourseEntity courseEntity) {
        if(userEntity.equals(courseEntity.getGuardianMain()))
            return true;

        return courseEntity.getUsersGuardians().stream().anyMatch(userEntity::equals);
    }


    /**
     * check if user "part" of given list
     * @param user user to be checked
     * @param list list to be checked on
     * @return true if is part of list
     */
    private boolean userOnList(UserEntity user, List<UserEntity> list) {
        return CollectionUtils.emptyIfNull(list).stream().anyMatch(user::equals);
    }


    /**
     * checks if user is already signed in given course, if it is not signs him in
     * @param user to be signed in
     * @param course to sign user in
     */
    public void signUserInCourse(UserEntity user, CourseEntity course) {
        var userCourses = user.getUsersCourses();
        boolean isSignedIn = userCourses.stream().anyMatch(course::equals);
        if(!isSignedIn) {
            userCourses.add(course);
            user.setUsersCourses(userCourses);

            List<UserEntity> users = course.getUsersSignedInCourse();
            users.add(user);
            course.setUsersSignedInCourse(users);
        }

        courseRepository.save(course);
    }

    /**
     * checks if given user can edit course (is either admin or guardian of course)
     * @param user to be checked
     * @param course to check
     * @return true if user can edit course
     */
    public boolean userCanEditCourse(UserEntity user, CourseEntity course) {
        if(userIsAdmin(user))
            return true;

        //dodaja se novega, preveri, če je ali administrator ali profesor
        if(course.getCourseId() == null)
            return userIsProfessor(user);

        return userOnList(user, course.getUsersGuardians()) || course.getGuardianMain().equals(user);
    }

}
