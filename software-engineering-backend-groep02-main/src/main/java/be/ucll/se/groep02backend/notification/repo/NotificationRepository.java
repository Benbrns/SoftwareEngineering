package be.ucll.se.groep02backend.notification.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import be.ucll.se.groep02backend.notification.model.Notification;
import be.ucll.se.groep02backend.rent.model.domain.Rent;
import be.ucll.se.groep02backend.rent.model.domain.RentStatus;
import be.ucll.se.groep02backend.user.model.User;

public interface NotificationRepository extends JpaRepository<Notification, Long>{
        public List<Notification> findAll();
        public Notification findNotificationById(Long id);
        public Notification findNotificationByRent(Rent rent);
        public List<Notification> findNotificationsByRentUserAndRenterViewedAndRentStatusNot(User user, boolean renterViewed, RentStatus Status);
        public List<Notification> findNotificationsByRentRentalCarUser(User owner);
        public List<Notification> findNotificationByRentUser(User user);

}
