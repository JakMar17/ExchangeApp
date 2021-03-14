package si.fri.jakmar.backend.exchangeapp.database.mysql.repositories.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.users.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    @Query("SELECT u FROM UserEntity u WHERE u.personalNumber = :personalNumber")
    List<UserEntity> findUsersByPersonalNumber(String personalNumber);

    @Query("SELECT u FROM UserEntity u WHERE u.username = :email")
    Optional<UserEntity> findUsersByEmail(String email);

    @Query("SELECT u FROM UserEntity u WHERE u.username = :email OR u.personalNumber = :personalNumber")
    Optional<UserEntity> findUsersByEmailOrPersonalNumber(String email, String personalNumber);

    @Query("SELECT u FROM UserEntity u WHERE u.userType.id = 2 ORDER BY u.userCreated DESC")
    List<UserEntity> getLastInsertedProfessor();

    @Query("SELECT u FROM UserEntity  u WHERE u.username = :email AND u.password = :password")
    List<UserEntity> getUserByEmailAndPassword(String email, String password);

    @Query("SELECT u FROM UserEntity u WHERE u.confirmationString = :confirmationString")
    UserEntity getUserByConfirmationString(String confirmationString);
}
