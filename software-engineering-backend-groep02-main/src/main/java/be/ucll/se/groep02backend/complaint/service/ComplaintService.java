package be.ucll.se.groep02backend.complaint.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.ucll.se.groep02backend.complaint.model.Complaint;
import be.ucll.se.groep02backend.complaint.repo.ComplaintRepository;
import be.ucll.se.groep02backend.user.model.Role;
import be.ucll.se.groep02backend.user.model.User;
import be.ucll.se.groep02backend.user.repo.UserRepository;

@Service
public class ComplaintService {
    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;

    public ComplaintService() {
    }

    public List<Complaint> getAllComplaints(User user) throws ComplaintServiceException {
        if (user.getRoles().contains(Role.ADMIN)) {
            return complaintRepository.findAll();
        } else {
            return complaintRepository.findBySenderEmail(user.getEmail());
        } 
    }

    public Complaint addComplaint(Complaint complaint) throws ComplaintServiceException {
        Optional<User> senderEmail = userRepository.findByEmail(complaint.getSenderEmail());
        Optional<User> receiverEmail = userRepository.findByEmail(complaint.getReceiverEmail());

        if (senderEmail.isPresent() && receiverEmail.isPresent()) {
            return complaintRepository.save(complaint);
        } else {
            throw new ComplaintServiceException("complaint", "Sender or receiver email does not exist.");
        }
    }

    public Complaint deleteComplaint(Long id, User user) throws ComplaintServiceException {
        Optional<Complaint> complaint = complaintRepository.findById(id);

        if (complaint.isPresent() && user.getRoles().contains(Role.ADMIN)) {
            complaintRepository.deleteById(id);
            return complaint.get();
        }
        else {
            throw new ComplaintServiceException("complaint", "Complaint does not exist or you are not an admin.");
        }
    }
    
}
