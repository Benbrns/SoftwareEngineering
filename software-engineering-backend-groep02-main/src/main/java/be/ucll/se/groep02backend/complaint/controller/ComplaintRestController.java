package be.ucll.se.groep02backend.complaint.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import be.ucll.se.groep02backend.complaint.model.Complaint;
import be.ucll.se.groep02backend.complaint.service.ComplaintService;
import be.ucll.se.groep02backend.complaint.service.ComplaintServiceException;
import be.ucll.se.groep02backend.config.ApplicationConfig;
import be.ucll.se.groep02backend.user.service.UserServiceException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Tag(name = "Complaint")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/complaint")

public class ComplaintRestController {
    @Autowired
    private ComplaintService complaintService;

    @GetMapping
    public List<Complaint> getAllComplaints() throws ComplaintServiceException, UserServiceException {
        return complaintService.getAllComplaints(ApplicationConfig.getAuthenticatedUser());
    }

    @PostMapping("/add")
    public Complaint addComplaint(@RequestBody @Valid Complaint complaint) throws ComplaintServiceException, UserServiceException {
        return complaintService.addComplaint(complaint);
    }

    @DeleteMapping("/delete/")
    public Complaint deleteComplaint(@RequestParam("complaintId") Long complaintId) throws ComplaintServiceException, UserServiceException {
        return complaintService.deleteComplaint(complaintId, ApplicationConfig.getAuthenticatedUser());
    }
    
}
