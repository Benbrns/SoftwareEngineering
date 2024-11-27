package be.ucll.se.groep02backend.rental.repo;

import be.ucll.se.groep02backend.rental.model.domain.Rental;
import be.ucll.se.groep02backend.user.model.User;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    public List<Rental> findAll();

    public Rental findRentalById(Long id);

    public Rental findRentalByRentsId(Long id);

    @Query("SELECT u FROM User u JOIN u.cars c JOIN c.rentals r WHERE r = :rental")
    User findUserByRentalsCar(@Param("rental") Rental rental);
    @Query("SELECT r FROM Rental r WHERE " +
            "(:searchStartDate IS NULL OR r.startDate >= :searchStartDate) " +
            "AND (:searchEndDate IS NULL OR r.endDate <= :searchEndDate) " +
            "AND (:searchCity IS NULL OR r.city = :searchCity)")
    List<Rental> findRentalsByCriteria(
            @Param("searchStartDate") LocalDate searchStartDate,
            @Param("searchEndDate") LocalDate searchEndDate,
            @Param("searchCity") String searchCity);
}
