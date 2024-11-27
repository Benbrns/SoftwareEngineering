package be.ucll.se.groep02backend.complaint.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import be.ucll.se.groep02backend.complaint.model.Complaint;

public interface ComplaintRepository extends JpaRepository<Complaint, Long>{
    public List<Complaint> findAll();
    public Optional<Complaint> findById(Long id);
    public List<Complaint> findBySenderEmail(String senderEmail);
} 
