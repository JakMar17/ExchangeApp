package si.fri.jakmar.backend.exchangeapp.services.notifications;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.fri.jakmar.backend.exchangeapp.database.entities.notifications.NotificationEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.database.repositories.NotificationRepository;
import si.fri.jakmar.backend.exchangeapp.dtos.notifications.NotificationDTO;
import si.fri.jakmar.backend.exchangeapp.services.courses.CoursesServices;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.DataNotFoundException;
import si.fri.jakmar.backend.exchangeapp.services.users.UserAccessServices;
import si.fri.jakmar.backend.exchangeapp.services.users.UserServices;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationsServices {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserAccessServices userAccessServices;
    @Autowired
    private UserServices userServices;
    @Autowired
    private CoursesServices coursesServices;

    /**
     * returns notification with given id
     * @param notificationId id to be searched
     * @return notification
     * @throws DataNotFoundException notification with given id is not found
     */
    public NotificationEntity getNotificationById(Integer notificationId) throws DataNotFoundException {
        var optional = notificationRepository.findById(notificationId);
        if(optional.isEmpty())
            throw new DataNotFoundException("Obvestilo s podanim IDjem ne obstaja");
        else
            return optional.get();
    }

    /**
     * @return returns list of notifications without course (to be shown on dashboard)
     */
    public List<NotificationDTO> getDashboardNotifications() {
        return CollectionUtils.emptyIfNull(notificationRepository.getDashboardNotifications()).stream()
                .map(NotificationDTO::castFromEntity)
                .collect(Collectors.toList());
    }

    /**
     * saves notification to database
     * @param notificationDTO data
     * @param courseId if null save to be shown on dashboard
     * @param personalNumber user who performs operation
     * @return saved notification
     * @throws DataNotFoundException data not found
     * @throws AccessForbiddenException user cannot perform operation
     */
    public NotificationDTO saveNotification(NotificationDTO notificationDTO, Integer courseId, String personalNumber) throws DataNotFoundException, AccessForbiddenException {
        var user = userServices.getUserByPersonalNumber(personalNumber);

        return NotificationDTO.castFromEntity(
                courseId != null
                    ? saveNotificationForCourse(notificationDTO, courseId, user)
                    : saveNotificationForDashboard(notificationDTO, user)
        );
    }

    /**
     * deletes notification with given id
     * @param notificationId to be deleted
     * @param courseId if null then on dashboard
     * @param personalNumber user performing operation
     * @throws DataNotFoundException data not found
     * @throws AccessForbiddenException user cannot perform operation
     */
    public void deleteNotification(Integer notificationId, Integer courseId, String personalNumber) throws DataNotFoundException, AccessForbiddenException {
        var user = userServices.getUserByPersonalNumber(personalNumber);
        var notification = getNotificationById(notificationId);

        if (courseId != null) {
            deleteNotificationForCourse(notification, courseId, user);
        } else {
            deleteNotificationForDashboard(notification, user);
        }
    }

    private NotificationEntity saveNotificationForCourse(NotificationDTO notificationDto, Integer courseId, UserEntity user) throws DataNotFoundException, AccessForbiddenException {
        var course = coursesServices.getCourseEntityById(courseId);
        if(!userAccessServices.userCanEditCourse(user, course))
            throw new AccessForbiddenException("Uporabnik nima pravic za izvedbo operacije");

        return notificationRepository.save(NotificationEntity.castFromDto(notificationDto, course, user));
    }

    private NotificationEntity saveNotificationForDashboard(NotificationDTO notificationDto, UserEntity user) throws AccessForbiddenException {
        if(!userAccessServices.userIsAdmin(user))
            throw new AccessForbiddenException("Uporabnik nima pravic za izvedbo operacije");

        return notificationRepository.save(NotificationEntity.castFromDto(notificationDto, null, user));
    }

    private void deleteNotificationForCourse(NotificationEntity notification, Integer courseId, UserEntity user) throws DataNotFoundException, AccessForbiddenException {
        var course = coursesServices.getCourseEntityById(courseId);
        if(!userAccessServices.userCanEditCourse(user, course))
            throw new AccessForbiddenException("Uporabnik nima pravic za izvedbo operacije");

        notificationRepository.markAsDeleted(notification);
    }

    private void deleteNotificationForDashboard(NotificationEntity notification, UserEntity user) throws AccessForbiddenException {
        if(!userAccessServices.userIsAdmin(user))
            throw new AccessForbiddenException("Uporabnik nima pravic za izvedbo operacije");

        notificationRepository.markAsDeleted(notification);
    }

}
