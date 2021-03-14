package si.fri.jakmar.backend.exchangeapp.database.mysql.repositories.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.users.UserPasswordResetEntity;

import java.util.Optional;

public interface UserPasswordResetRepository extends CrudRepository<UserPasswordResetEntity, Integer> {

    @Query("SELECT u FROM UserPasswordResetEntity u WHERE u.resetKey = :resetKey")
    Optional<UserPasswordResetEntity> getByResetKey(String resetKey);
}
