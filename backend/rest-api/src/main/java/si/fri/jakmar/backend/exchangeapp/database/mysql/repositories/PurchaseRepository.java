package si.fri.jakmar.backend.exchangeapp.database.mysql.repositories;

import org.springframework.data.repository.CrudRepository;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.purchases.PurchaseEntity;

public interface PurchaseRepository extends CrudRepository<PurchaseEntity, Integer> {
}
