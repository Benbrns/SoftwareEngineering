package be.ucll.se.groep02backend.rent.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import be.ucll.se.groep02backend.rent.model.domain.Rent;
import be.ucll.se.groep02backend.user.model.User;

public interface RentRepository extends JpaRepository<Rent, Long> {
    public List<Rent> findAll();

    public Rent findRentByIdAndUserEmail(Long id, String email);

    public Rent findRentById(Long id);

    public List<Rent> findRentsByUserEmail(String email);

    public List<Rent> findRentByUser(User user);

    @Query("SELECT u FROM User u JOIN u.cars c JOIN c.rentals r  JOIN r.rents re WHERE re = :rent")
    public User findByRentalCarUser(Rent rent);
}
