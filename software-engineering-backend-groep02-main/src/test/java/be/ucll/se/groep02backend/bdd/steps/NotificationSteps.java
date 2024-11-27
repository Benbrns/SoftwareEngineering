package be.ucll.se.groep02backend.bdd.steps;

import java.time.LocalDate;

import org.checkerframework.checker.units.qual.s;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import be.ucll.se.groep02backend.Groep02BackendApplication;

import be.ucll.se.groep02backend.notification.model.Notification;
import be.ucll.se.groep02backend.notification.repo.NotificationRepository;
import be.ucll.se.groep02backend.rent.model.domain.Rent;
import be.ucll.se.groep02backend.rent.repo.RentRepository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;

@SpringBootTest(classes = Groep02BackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class NotificationSteps {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient client;

    private String token;

    private WebTestClient.ResponseSpec response;

    @Given("there are some notifications")
    public void there_are_some_notifications() {
        Rent rent1 = new Rent(LocalDate.now(), LocalDate.now().plusDays(7));
        Rent rent2 = new Rent(LocalDate.now(), LocalDate.now().plusDays(14));

        rent1 = rentRepository.save(rent1);
        rent2 = rentRepository.save(rent2);

        notificationRepository.save(new Notification(true, false, rent1));
        notificationRepository.save(new Notification(false, false, rent2));

        System.out.println("Saved notifications: " + notificationRepository.findAll());

    }

    @When("I want to get all notifications")
    public void i_want_to_get_all_notifications() {
        WebTestClient.ResponseSpec registerResponse = client.post()
                .uri("/register")
                .header("Content-Type", "application/json")
                .bodyValue("""
                        {
                            "email": "Jarne.vd@example.com",
                            "password": "wachwtoord!456",
                            "firstName": "Jarne",
                            "lastName": "Achternaam",
                            "isRenter": false,
                            "isOwner": true,
                            "phoneNumber": "0487660088",
                            "birthDate": "2003-12-12",
                            "nationalRegisterNumber": "03.12.12-456.78",
                            "licenseNumber": "0987654569"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isOk();

        String responseBody = registerResponse.returnResult(String.class).getResponseBody().blockFirst();
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            token = jsonNode.get("token").asText();
        } catch (Exception e) {
            System.out.println("WE ZIJN TOT HIER GERAAKT!!");
        }

        response = client.get()
                .uri("/notification")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token).exchange()
                .expectStatus().isOk();

    }

    @Then("the data of all notifications is returned in json format")
    public void the_data_of_all_notifications_is_returned_in_json_format() {
        response.expectStatus().isOk().expectBody()
                .json("[]");
        // {\"renterViewed\":\"true\",
        // \"ownerViewed\":\"false\"},{\"renterViewed\":\"false\",
        // \"ownerViewed\":\"false\"}

    }

}
