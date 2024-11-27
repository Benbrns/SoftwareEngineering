package be.ucll.se.groep02backend.notification.model;

import be.ucll.se.groep02backend.rent.model.domain.Rent;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
@Table(name = "notification")
public class Notification {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public long id;

    private boolean renterViewed;
    private boolean ownerViewed;

    @OneToOne(optional = true)
    private Rent rent;

    public Notification() {
    }

    public Notification(boolean renterViewed, boolean ownerViewed, Rent rent) {
        setOwnerViewed(ownerViewed);
        setReceiverViewed(renterViewed);
        setrent(rent);
    }

    public long getId() {
        return this.id;
    }
    
    public boolean getReceiverViewed() {
        return this.renterViewed;
    }

    public boolean getOwnerViewed() {
        return this.ownerViewed;
    }

    public Rent getRent() {
        return this.rent;
    }

    public void setReceiverViewed(boolean renterViewed) {
        this.renterViewed = renterViewed;
    }

    public void setOwnerViewed(boolean ownerViewed) {
        this.ownerViewed = ownerViewed;
    }

    public void setrent(Rent rent) {
        this.rent = rent;
    }





    

}
