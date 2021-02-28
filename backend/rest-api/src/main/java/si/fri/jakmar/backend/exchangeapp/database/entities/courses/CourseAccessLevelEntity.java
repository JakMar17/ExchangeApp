package si.fri.jakmar.backend.exchangeapp.database.entities.courses;

import org.hibernate.annotations.Tables;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "course_access_level")
public class CourseAccessLevelEntity {

    @Id
    @Column(name="access_level_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="access_level_description")
    private String description;

    @OneToMany(mappedBy = "accessLevel")
    private List<CourseEntity> courses;

    public CourseAccessLevelEntity() {
    }

    public CourseAccessLevelEntity(Integer id) {
        this.id = id;
    }

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
