package si.fri.jakmar.backend.exchangeapp.services.users;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.fri.jakmar.backend.exchangeapp.database.entities.courses.CourseEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.database.repositories.UserRepository;
import si.fri.jakmar.backend.exchangeapp.exceptions.DataNotFoundException;

@Component
public class UserServices {

    @Autowired
    private UserRepository userRepository;

    /**
     * gets user from database by personal number or throws exception DataNotFound
     * @param personalNumber users personal number
     * @return UserEntity
     * @throws DataNotFoundException user with given personal number doesnt exists
     */
    public UserEntity getUserByPersonalNumber(String personalNumber) throws DataNotFoundException {
        var users = userRepository.findUsersByPersonalNumber(personalNumber);
        if(users == null || users.size() == 0)
            throw new DataNotFoundException("Uporabnik s podano številko ne obstaja");
        else
            return users.get(0);
    }

    /**
     * gets user from database by email or throws exception DataNotFound
     * @param email users email
     * @return UserEntity
     * @throws DataNotFoundException user with given email doesnt exists
     */
    public UserEntity getUserByEmail(String email) throws DataNotFoundException {
        var users = userRepository.findUsersByEmail(email);
        if(users == null || users.size() == 0)
            throw new DataNotFoundException("Uporabnik s podanim epoštnim naslovom ne obstaja");
        else
            return users.get(0);
    }

    /**
     * gets user from database by personal number or (if personal number is not given) by email or throws exception DataNotFound
     * @param personalNumber users personal number
     * @param email users email
     * @return UserEntity
     * @throws DataNotFoundException user with given personal number/email doesnt exists
     */
    public UserEntity getUserByEmailOrPersonalNumber(String personalNumber, String email) throws DataNotFoundException {
        UserEntity u = null;
        if(personalNumber != null)
            u =  getUserByPersonalNumber(personalNumber);
        else
            u = getUserByEmail(email);

        return u;
    }

    public Integer getUsersCoinsInCourse(UserEntity user, CourseEntity course) {
        int coins = course.getInitialCoins();

        var usersSubmissionsInCourse = CollectionUtils.emptyIfNull(user.getSubmissions()).stream()
                .filter(e -> e.getAssignment().getCourse().equals(course));

        var usersPurchaseInCourse = CollectionUtils.emptyIfNull(user.getPurchases()).stream()
                .filter(e -> e.getSubmissionBought().getAssignment().getCourse().equals(course));

        coins += usersSubmissionsInCourse.mapToInt(e -> e.getAssignment().getCoinsPerSubmission()).sum();
        coins -= usersPurchaseInCourse.mapToInt(e -> e.getSubmissionBought().getAssignment().getCoinsPrice()).sum();

        return coins;
    }
}
