package si.fri.jakmar.backend.exchangeapp.database.mysql.entities.users;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "user_password_reset")
public class UserPasswordResetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reset_id")
    private Integer id;

    @Column(name = "reset_key")
    private String resetKey;
    @Column(name = "reset_key_created")
    private LocalDateTime created = LocalDateTime.now(ZoneOffset.UTC);
    @Column(name = "reset_key_used")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean used = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public UserPasswordResetEntity() {
    }

    public UserPasswordResetEntity(String resetKey, UserEntity user) {
        this.resetKey = resetKey;
        this.user = user;
    }

    public boolean isActive() {
        return this.created.plus(Duration.ofHours(1)).isAfter(LocalDateTime.now(ZoneOffset.UTC)) && !used;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }
}
