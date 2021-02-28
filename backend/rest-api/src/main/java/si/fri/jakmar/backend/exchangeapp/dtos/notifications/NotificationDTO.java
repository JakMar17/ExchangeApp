package si.fri.jakmar.backend.exchangeapp.dtos.notifications;

import si.fri.jakmar.backend.exchangeapp.database.entities.notifications.NotificationEntity;
import si.fri.jakmar.backend.exchangeapp.dtos.users.UserDTO;

import java.time.LocalDateTime;

public class NotificationDTO {
    private Integer notificationId;
    private String title;
    private String body;
    private LocalDateTime created;
    private UserDTO author;

    public NotificationDTO() {
    }

    private NotificationDTO(Integer notificationId, String title, String body, LocalDateTime created, UserDTO author) {
        this.notificationId = notificationId;
        this.title = title;
        this.body = body;
        this.created = created;
        this.author = author;
    }

    public static NotificationDTO castFromEntity(NotificationEntity entity) {
        return new NotificationDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getBody(),
                entity.getCreated(),
                UserDTO.castFromEntityWithoutCourses(entity.getAuthor())
        );
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
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

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }
}
