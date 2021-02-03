package si.fri.jakmar.backend.exchangeapp.database.entities.users;

import javax.persistence.*;

@Entity
@Table(name = "user_type")
public class UserTypeEntity {

    @Id
    @Column(name = "user_type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_type_description")
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}