package si.fri.jakmar.backend.exchangeapp.database.entities.notifications;

import org.hibernate.annotations.Type;
import si.fri.jakmar.backend.exchangeapp.api.notifications.NotificationsApi;
import si.fri.jakmar.backend.exchangeapp.database.entities.courses.CourseEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.dtos.notifications.NotificationDTO;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "notification")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Integer id;

    @Column(name = "notification_title")
    private String title;
    @Column(name = "notification_body")
    private String body;
    @Column(name = "notification_created")
    private LocalDateTime created = LocalDateTime.now(ZoneOffset.UTC);
    @Column(name = "notification_deleted")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseEntity course;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity author;

    public NotificationEntity() {
    }

    public NotificationEntity(Integer id, String title, String body, CourseEntity course, UserEntity author) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.course = course;
        this.author = author;
    }

    public static NotificationEntity castFromDto(NotificationDTO dto, CourseEntity course, UserEntity author) {
        return new NotificationEntity(
                dto.getNotificationId(),
                dto.getTitle(),
                dto.getBody(),
                course,
                author
        );
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public CourseEntity getCourse() {
        return course;
    }

    public void setCourse(CourseEntity course) {
        this.course = course;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
