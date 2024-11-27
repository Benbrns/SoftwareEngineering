package be.ucll.se.groep02backend.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class RentalIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private WebTestClient client;

    private String token;
    private WebTestClient.ResponseSpec response;

    @Test
    public void getRentalsTest(){
        WebTestClient.ResponseSpec registerResponse = client.post()
        .uri("/register")
        .header("Content-Type", "application/json")
        .bodyValue("""
        {
            "email": "example@matteo.com",
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

        response = (ResponseSpec) client.get()
                .uri("/rental")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk();
    }

    // @Test
    // public void addRentalTest() throws Exception {
    //     // Step 1: Register a user
    //     WebTestClient.ResponseSpec registerResponse = client.post()
    //         .uri("/register")
    //         .header("Content-Type", "application/json")
    //         .bodyValue("""
    //         {
    //             "email": "alice.williams@example.com",
    //             "password": "SuperSecure!456",
    //             "firstName": "Alice",
    //             "lastName": "Williams",
    //             "isRenter": false,
    //             "isOwner": true,
    //             "phoneNumber": "0987654321",
    //             "birthDate": "1988-12-12",
    //             "nationalRegisterNumber": "88.12.12-456.78",
    //             "licenseNumber": "0987654321"
    //         }
    //         """)
    //         .exchange()
    //         .expectStatus()
    //         .isOk();

    //     String responseBody = registerResponse.returnResult(String.class).getResponseBody().blockFirst();
    //     String token = "";
    //     try {
    //         JsonNode jsonNode = objectMapper.readTree(responseBody);
    //         token = jsonNode.get("token").asText();
    //     } catch (Exception e) {
    //         System.out.println("Error parsing response body");
    //         e.printStackTrace();
    //     }

    //     // Step 2: Add a car
    //     WebTestClient.ResponseSpec addCarResponse = client.post()
    //         .uri("/car/add")
    //         .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
    //         .header("Content-Type", "application/json")
    //         .bodyValue("""
    //         {
    //             "brand": "Toyota",
    //             "model": "Corolla",
    //             "type": "Sedan",
    //             "licensePlate": "XYZ1234",
    //             "numberOfSeats": 5,
    //             "numberOfChildSeats": 2,
    //             "foldingRearSeat": true,
    //             "towBar": false
    //         }  
    //         """)
    //         .exchange()
    //         .expectStatus()
    //         .isOk();

    //     // Retrieve the car ID from the response (assuming it's included)
    //     String carId = addCarResponse.returnResult(String.class).getResponseBody().blockFirst();
    //     JsonNode carNode = objectMapper.readTree(carId);
    //     carId = carNode.get("id").asText();

    //     // Step 3: Add a rental
    //     WebTestClient.ResponseSpec addRentalResponse = client.post()
    //         .uri("/rental/add")
    //         .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
    //         .header("Content-Type", "application/json")
    //         .bodyValue("""
    //         {
    //             "carId": \"" + carId + "\",
    //             "startDate": "2024-07-01",
    //             "endDate": "2024-07-10",
    //             "street": "Main Street",
    //             "streetNumber": 123,
    //             "postal": 12345,
    //             "city": "Anytown",
    //             "basePrice": 50.0,
    //             "pricePerKm": 0.25,
    //             "fuelPenaltyPrice": 100.0,
    //             "pricePerDay": 25.0
    //         }
    //         """)
    //         .exchange()
    //         .expectStatus()
    //         .isForbidden();
    // }

}
