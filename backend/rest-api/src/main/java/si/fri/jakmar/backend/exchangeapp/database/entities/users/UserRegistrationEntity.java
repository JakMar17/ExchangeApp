package si.fri.jakmar.backend.exchangeapp.database.entities.users;

import javax.persistence.*;

@Entity
@Table(name = "user_registration")
public class UserRegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_registration_id")
    private Integer id;

    //@Column(name = "")
}
