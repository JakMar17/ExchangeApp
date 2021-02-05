package si.fri.jakmar.backend.exchangeapp.services.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.fri.jakmar.backend.exchangeapp.database.entities.courses.CourseEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.database.repositories.UserRepository;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.AccessUnauthorizedException;

import java.util.List;

@Component
public class UserAccessServices {

    @Autowired
    private UserRepository userRepository;

    public boolean userHasAccessToCourse(UserEntity userEntity, CourseEntity courseEntity) throws AccessForbiddenException, AccessUnauthorizedException {
        if(userIsAdmin((userEntity)))
            return true;

        if(userIsGuardian(userEntity, courseEntity))
            return true;

        //check if on blacklist
        if(userOnList(userEntity, courseEntity.getUsersBlacklisted()))
            throw new AccessForbiddenException("Uporabniku je blokiran dostop do predmeta");

        switch (courseEntity.getAccessLevel().getDescription()) {
            case "WHITELIST":
                if(userOnList(userEntity, courseEntity.getUsersWhitelisted()))
                    return true;
                else
                    throw new AccessForbiddenException("Uporabnik ni dodan med izjeme, ki imajo dostop do predmeta");
            case "PASSWORD":
                if(userOnList(userEntity, courseEntity.getUsersSignedInCourse()))
                    return true;
                else
                    throw new AccessUnauthorizedException("Predmet je zaščiten z geslom, vnesite ga preden lahko nadaljujete");
            default:
                return true;
        }
    }

    private boolean userIsAdmin(UserEntity userEntity) {
        return userEntity.getUserType().getId() == 1;
    }

    private boolean userIsGuardian(UserEntity userEntity, CourseEntity courseEntity) {
        if(userEntity.equals(courseEntity.getGuardianMain()))
            return true;

        for(var u : courseEntity.getUsersGuardians())
            if(userEntity.equals(u))
                return true;

        return false;
    }

    private boolean userOnList(UserEntity user, List<UserEntity> list) {
        for(var u : list)
            if(user.equals(u))
                return true;
        return false;
    }

    private UserEntity signUserInCourse(UserEntity user, CourseEntity course) {
        var userCourses = user.getUsersCourses();
        userCourses.add(course);
        user.setUsersCourses(
                userCourses
        );

        return userRepository.save(user);
    }

}
