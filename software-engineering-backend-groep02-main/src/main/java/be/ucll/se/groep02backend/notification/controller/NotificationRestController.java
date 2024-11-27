package be.ucll.se.groep02backend.notification.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be.ucll.se.groep02backend.config.ApplicationConfig;
import be.ucll.se.groep02backend.notification.service.NotificationService;
import be.ucll.se.groep02backend.notification.service.NotificationServiceException;
import be.ucll.se.groep02backend.notification.service.PublicNotification;
import be.ucll.se.groep02backend.user.service.UserServiceException;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Notification")
@RestController
@RequestMapping("/notification")
public class NotificationRestController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping()
    public List<PublicNotification> getMethodName() throws NotificationServiceException, UserServiceException {
        return notificationService.getAllNotifications(ApplicationConfig.getAuthenticatedUser());
    }

    @PutMapping("/viewed/owner/{notificationId}")
    public String viewedNotificationOwner(@PathVariable("notificationId") Long notificationId)
            throws NotificationServiceException, UserServiceException {
        return notificationService.ownerViewed(notificationId, ApplicationConfig.getAuthenticatedUser());
    }

    @PutMapping("/viewed/renter/{notificationId}")
    public String viewedNotificationRenter(@PathVariable("notificationId") Long notificationId)
            throws NotificationServiceException, UserServiceException {
        return notificationService.renterViewed(notificationId, ApplicationConfig.getAuthenticatedUser());
    }

}
