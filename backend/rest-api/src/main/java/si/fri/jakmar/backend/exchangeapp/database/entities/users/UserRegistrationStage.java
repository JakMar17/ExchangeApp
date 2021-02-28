package si.fri.jakmar.backend.exchangeapp.database.entities.users;

import javax.persistence.*;

@Entity
@Table(name = "user_registration_status")
public class UserRegistrationStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registration_status_id")
    private Integer id;

    @Column(name = "registration_status")
    private String status;

    public UserRegistrationStage() {
    }

    public UserRegistrationStage(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
