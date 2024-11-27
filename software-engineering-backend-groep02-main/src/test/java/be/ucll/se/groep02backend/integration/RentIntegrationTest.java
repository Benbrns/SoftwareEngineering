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

public class RentIntegrationTest {
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private WebTestClient client;

    private String token;
    private WebTestClient.ResponseSpec response;

    @Test
    public void getRentTest(){
        WebTestClient.ResponseSpec registerResponse = client.post()
        .uri("/register")
        .header("Content-Type", "application/json")
        .bodyValue("""
        {
            "email": "emma.brown@example.com",
            "password": "AnotherSecurePassword!789",
            "firstName": "Emma",
            "lastName": "Brown",
            "isRenter": false,
            "isOwner": true,
            "phoneNumber": "0123456789",
            "birthDate": "1990-06-25",
            "nationalRegisterNumber": "90.06.25-789.01",
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
                .uri("/rent")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk();
    }

}
