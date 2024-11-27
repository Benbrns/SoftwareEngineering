package be.ucll.se.groep02backend.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class CarIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private WebTestClient client;

    private String token;
    private WebTestClient.ResponseSpec response;
    
    @Test
    public void getAllCarTest(){
        WebTestClient.ResponseSpec registerResponse = client.post()
        .uri("/register")
        .header("Content-Type", "application/json")
        .bodyValue("""
        {
            "email": "john.doe@example.com",
            "password": "StrongPassword!2024",
            "firstName": "Jane",
            "lastName": "Smith",
            "isRenter": false,
            "isOwner": true,
            "phoneNumber": "9876543210",
            "birthDate": "1985-05-15",
            "nationalRegisterNumber": "85.05.15-678.90",
            "licenseNumber": "9876543210"
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
                .uri("/car")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void addCarTest(){
        WebTestClient.ResponseSpec registerResponse = client.post()
        .uri("/register")
        .header("Content-Type", "application/json")
        .bodyValue("""
        {
            "email": "alice.williams@example.com",
            "password": "SuperSecure!456",
            "firstName": "Alice",
            "lastName": "Williams",
            "isRenter": false,
            "isOwner": true,
            "phoneNumber": "0987654321",
            "birthDate": "1988-12-12",
            "nationalRegisterNumber": "88.12.12-456.78",
            "licenseNumber": "0987654321"
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

        response = client.post()
                .uri("/car/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .header("Content-Type", "application/json")
                .bodyValue("""
                {
                    "brand": "Toyota",
                    "model": "Corolla",
                    "type": "Sedan",
                    "licensePlate": "XYZ1234",
                    "numberOfSeats": 5,
                    "numberOfChildSeats": 2,
                    "foldingRearSeat": true,
                    "towBar": false
                }  
                """)
                .exchange()
                .expectStatus()
                .isOk();
    }
}
