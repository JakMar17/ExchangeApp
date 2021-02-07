package si.fri.jakmar.backend.exchangeapp.database.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    @Query("SELECT u FROM UserEntity u WHERE u.personalNumber = :personalNumber")
    List<UserEntity> findUsersByPersonalNumber(String personalNumber);

    @Query("SELECT u FROM UserEntity u WHERE u.email = :email")
    List<UserEntity> findUsersByEmail(String email);

    @Query("SELECT u FROM UserEntity u WHERE u.userType.id = 2 ORDER BY u.userCreated DESC")
    List<UserEntity> getLastInsertedProfessor();

    @Query("SELECT u FROM UserEntity  u WHERE u.email = :email AND u.password = :password")
    List<UserEntity> getUserByEmailAndPassword(String email, String password);

}
