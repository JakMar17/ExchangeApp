package si.fri.jakmar.backend.exchangeapp.services.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import si.fri.jakmar.backend.exchangeapp.database.entities.purchases.PurchaseEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.submissions.SubmissionEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.database.repositories.PurchaseRepository;

@Service
public class PurchaseServices {

    @Autowired
    private PurchaseRepository purchaseRepository;

    public PurchaseEntity savePurchase(UserEntity userEntity, SubmissionEntity submissionEntity) {
        return purchaseRepository.save(new PurchaseEntity(userEntity, submissionEntity));
    }
}
