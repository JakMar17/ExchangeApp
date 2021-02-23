package si.fri.jakmar.backend.exchangeapp.database.entities.courses;

import javax.persistence.*;

@Entity
@Table(name = "course_access_password")
public class CourseAccessPassword {

    @Id
    @Column(name ="access_password_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "access_password")
    private String password;

    @OneToOne(mappedBy = "accessPassword")
    private CourseEntity course;

    public CourseAccessPassword() {
    }

    public CourseAccessPassword(String password) {
        this.password = password;
    }

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
