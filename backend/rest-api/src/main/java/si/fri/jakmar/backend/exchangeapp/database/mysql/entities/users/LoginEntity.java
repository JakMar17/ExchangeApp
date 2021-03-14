package si.fri.jakmar.backend.exchangeapp.database.mysql.entities.users;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Data
@Table(name = "login")
public class LoginEntity {

    @Id
    @Column(name = "login_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "login_ok")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean loginOk;
    @Column(name = "login_ip")
    private String ipAddress;
    @Column(name = "login_date")
    private LocalDateTime loginDate = LocalDateTime.now(ZoneOffset.UTC);

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
