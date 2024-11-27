package be.ucll.se.groep02backend.bdd.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import be.ucll.se.groep02backend.Groep02BackendApplication;
import be.ucll.se.groep02backend.car.model.Car;
import be.ucll.se.groep02backend.rental.model.domain.Rental;
import be.ucll.se.groep02backend.rental.repo.RentalRepository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;


import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

@SpringBootTest(classes = Groep02BackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RentalSteps {
    @Autowired
    private WebTestClient client;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @Autowired
    private RentalRepository rentalRepository;

    private WebTestClient.ResponseSpec response;
    private Rental rental = new Rental();

    @Before
    public void setup(){}

    @Given("some rentals")
    public void some_rentals() {       
    }
    @When("the user requests all rentals")
    public void the_user_requests_all_rentals() {
        WebTestClient.ResponseSpec registerResponse = client.post()
        .uri("/register")
        .header("Content-Type", "application/json")
        .bodyValue("""
        {
            "email": "test@matteo.com",
            "password": "securePassword123",
            "firstName": "John",
            "lastName": "Doe",
            "isRenter": true,
            "isOwner": false,
            "phoneNumber": "1234567890",
            "birthDate": "1990-01-01",
            "nationalRegisterNumber": "90.01.01-123.45",
            "licenseNumber": "1234567890"
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
            System.out.println("Test");
        }

        response = client.get()
        .uri("/rental")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .exchange();
    }
    @Then("all rents are returned")
    public void all_rents_are_returned() {
        response.expectStatus().isOk();
    }
}
