package be.ucll.se.groep02backend.bill.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import be.ucll.se.groep02backend.bill.model.Bill;

public interface BillRepository extends JpaRepository<Bill, Long>{
    public List<Bill> findAll();

    public Bill findBillById(long id);

    @Query("SELECT b FROM Bill b WHERE b.renterEmail = ?1 OR b.ownerEmail = ?1")
    List<Bill> findAllBillsByRenterEmailOrOwnerEmail(String email);
}
