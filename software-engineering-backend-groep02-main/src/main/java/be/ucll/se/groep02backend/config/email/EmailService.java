package be.ucll.se.groep02backend.config.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import be.ucll.se.groep02backend.car.model.Car;
import be.ucll.se.groep02backend.rent.model.domain.RentStatus;
import be.ucll.se.groep02backend.user.model.Role;
import be.ucll.se.groep02backend.user.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    private final static String baseURL = "https://groep02-frontend-acceptance.azurewebsites.net";

    public String readFile(String path) throws IOException {
        Path filePath = Paths.get(path);
        return Files.readString(filePath, StandardCharsets.UTF_8);
    }

    public void sendWelcomeEmail(User user) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.setFrom("carrentalucll@gmail.com");
        message.setRecipients(RecipientType.TO, user.getEmail());
        message.setSubject("Welcome to Car Rental UCLL");

        String welcome = "Welcome, " + user.getFirstName() + " " + user.getLastName() + "!";
        String buttonText = "Start renting</a>";
        String buttonLink = baseURL;
        String content = "We are happy to welcome you to our platform. We hope you will have a great experience with us. If you have any questions, feel free to contact us. Enjoy your day!";
        String greyContend = "";

        if (user.getRoles().contains(Role.RENTER)) {
            buttonText = "Start renting";
            buttonLink = baseURL + "";
            content = content + "<br><br>You are signed up as a renter.";
        }
        if (user.getRoles().contains(Role.OWNER)) {
            buttonText = "Start renting out cars";
            buttonLink = baseURL + "";
            content = content + "<br><br>You are signed up as an owner.";
        }
        if (user.getRoles().contains(Role.ACCOUNTANT)) {
            buttonText = "Start accounting";
            buttonLink = baseURL + "";
            content = content + "<br><br>You are signed up as an accountant.";
        }
        if (user.getRoles().contains(Role.ADMIN)) {
            buttonText = "Start managing the platform";
            buttonLink = baseURL + "";
            content = content + "<br><br>You are signed up as an admin.";
        }

        String html = readFile("src/main/java/be/ucll/se/groep02backend/config/email/html/basic.html");
        String finalHtml = html
                .replace("${title}", welcome)
                .replace("${content}", content)
                .replace("${buttonText}", buttonText)
                .replace("${buttonLink}", buttonLink)
                .replace("${greyContent}", greyContend);

        message.setContent(finalHtml, "text/html; charset=utf-8");

        javaMailSender.send(message);
    }

    

    public void sendRenterStatusUpdateEmail(User user, RentStatus status, Car car, LocalDate startdate, LocalDate endDate)
            throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();

        String buttonLink = "";
        String welcome = "";
        String content = "";
        String buttonText = "";
        String greyContend = "";

        message.setFrom("carrentalucll@gmail.com");
        message.setRecipients(RecipientType.TO, user.getEmail());
        if (status == RentStatus.CONFIRMED) {
            message.setSubject("Your rent has been approved!");
            buttonLink = baseURL + "/rents";
            welcome = "Your rent has been approved 🎉";
            content = "We are happy to inform you that your rent for car: " + car.getBrand() + " " +  car.getModel() + " for given period:  "
                    + startdate.getDayOfMonth()+ "/" + startdate.getMonthValue() + " - " + endDate.getDayOfMonth()+ "/" + endDate.getMonthValue() + " has been approved. Enjoy your ride!";
            buttonText = "View rent";
            greyContend = "If you have any questions, feel free to contact us.";
        } else if (status == RentStatus.REJECTED) {
            message.setSubject("Your rent has been declined");
            buttonLink = baseURL + "/rentals";
            welcome = "Your rent has been declined 😟";
            content = "We are sorry to inform you that your rent for car: " + car.getBrand() + " " +  car.getModel() +  " for given period:  "
                    + startdate.getDayOfMonth()+ "/" + startdate.getMonthValue() + " - " + endDate.getDayOfMonth()+ "/" + endDate.getMonthValue() + " has been declined. Try again with another car!";
            buttonText = "Rent another car";
            greyContend = "If you have any questions, feel free to contact us.";

        }

        String html = readFile("src/main/java/be/ucll/se/groep02backend/config/email/html/basic.html");
        String finalHtml = html
                .replace("${title}", welcome)
                .replace("${content}", content)
                .replace("${buttonText}", buttonText)
                .replace("${buttonLink}", buttonLink)
                .replace("${greyContent}", greyContend);

        message.setContent(finalHtml, "text/html; charset=utf-8");

        javaMailSender.send(message);
    }

    
    public void sendOwnerEmailWhenNewRent(User owner, Car car, User renter)
            throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();

        String buttonLink = "";
        String welcome = "";
        String content = "";
        String buttonText = "";
        String greyContend = "";

        message.setFrom("carrentalucll@gmail.com");
        message.setRecipients(RecipientType.TO, owner.getEmail());

        message.setSubject("New rent request for your car!");

        buttonLink = baseURL;
        welcome = "New rent request for your car!";
        content = "You have received a new rent request for your car: " + car.getBrand() + " " +  car.getModel() + " from: " + renter.getFirstName() + ". Please check the platform to confirm or reject the request.";
        buttonText = "View rent request";
        greyContend = "If you have any questions, feel free to contact us.";

        String html = readFile("src/main/java/be/ucll/se/groep02backend/config/email/html/basic.html");
        String finalHtml = html
                .replace("${title}", welcome)
                .replace("${content}", content)
                .replace("${buttonText}", buttonText)
                .replace("${buttonLink}", buttonLink)
                .replace("${greyContent}", greyContend);

        message.setContent(finalHtml, "text/html; charset=utf-8");

        javaMailSender.send(message);
    }
}
