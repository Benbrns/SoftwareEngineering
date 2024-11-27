package be.ucll.se.groep02backend.complaint.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "complaint")
public class Complaint {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @NotBlank(message = "Writer email is required")
    private String senderEmail;

    @NotBlank(message = "Receiver email is required")
    private String receiverEmail;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    public Complaint() {
    }

    public Complaint(String senderEmail, String receiverEmail, String title, String description) {
        setSenderEmail(receiverEmail);
        setReceiverEmail(receiverEmail);
        setTitle(title);
        setDescription(description);
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String writerEmail) {
        this.senderEmail = writerEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
