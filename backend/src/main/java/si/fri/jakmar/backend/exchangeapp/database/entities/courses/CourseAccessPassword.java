package si.fri.jakmar.backend.exchangeapp.database.entities.courses;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "course_access_password")
public class CourseAccessPassword {

    @Id
    @Column(name ="access_password_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "access_password")
    private String password;

    @OneToMany(mappedBy = "accessPassword")
    List<CourseEntity> courses;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
