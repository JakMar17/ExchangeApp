package si.fri.jakmar.backend.exchangeapp.database.mysql.repositories.user;

import org.springframework.data.repository.CrudRepository;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.users.LoginEntity;

public interface LoginRepository extends CrudRepository<LoginEntity, Integer> {
}
