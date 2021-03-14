package si.fri.jakmar.backend.exchangeapp.database.mysql.entities.assignments;

import javax.persistence.*;

@Entity
@Table(name = "submission_check_url")
public class SubmissionCheckUrl {

    @Id
    @Column(name = "submission_check_url_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "check_url")
    private String url;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
