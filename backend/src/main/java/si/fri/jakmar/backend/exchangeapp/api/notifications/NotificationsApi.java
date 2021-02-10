package si.fri.jakmar.backend.exchangeapp.api.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.fri.jakmar.backend.exchangeapp.api.wrappers.exceptions.ExceptionWrapper;
import si.fri.jakmar.backend.exchangeapp.dtos.notifications.NotificationDTO;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.DataNotFoundException;
import si.fri.jakmar.backend.exchangeapp.services.notifications.NotificationsServices;

import java.util.logging.Logger;

@RestController
@RequestMapping("/notification")
@CrossOrigin("*")
public class NotificationsApi {

    private static final Logger logger = Logger.getLogger(NotificationsApi.class.getSimpleName());

    @Autowired
    private NotificationsServices notificationsServices;

    @GetMapping("/dashboard")
    public ResponseEntity<Object> getDashboardNotification() {
        return ResponseEntity.ok(notificationsServices.getDashboardNotifications());
    }

    @PostMapping
    public ResponseEntity<Object> saveNotification(@RequestHeader(name = "Personal-Number") String personalNumber, @RequestHeader(name = "Course-Id", required = false) Integer courseId, @RequestBody NotificationDTO notification) {
        try {
            return ResponseEntity.ok(notificationsServices.saveNotification(notification, courseId, personalNumber));
        } catch (DataNotFoundException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionWrapper(e.getMessage()));
        } catch (AccessForbiddenException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionWrapper(e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteNotification(@RequestHeader(name = "Personal-Number") String personalNumber, @RequestHeader(name = "Course-Id", required = false) Integer courseId, @RequestParam Integer notificationId) {
        try {
            notificationsServices.deleteNotification(notificationId, courseId, personalNumber);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (DataNotFoundException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionWrapper(e.getMessage()));
        } catch (AccessForbiddenException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionWrapper(e.getMessage()));
        }
    }
}
