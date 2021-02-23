package si.fri.jakmar.backend.exchangeapp.database.repositories;

import org.springframework.data.repository.CrudRepository;
import si.fri.jakmar.backend.exchangeapp.database.entities.purchases.PurchaseEntity;

public interface PurchaseRepository extends CrudRepository<PurchaseEntity, Integer> {
}
