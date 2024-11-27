package be.ucll.se.groep02backend.bdd.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import be.ucll.se.groep02backend.Groep02BackendApplication;
import be.ucll.se.groep02backend.car.model.Car;
import be.ucll.se.groep02backend.car.repo.CarRepository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = Groep02BackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CarSteps {
    @Autowired
    private WebTestClient client;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @Autowired
    private CarRepository carRepository;

    private WebTestClient.ResponseSpec response;
    private Car car = new Car();
    @Before
    public void setup(){ Car car = new Car();}
    @Given("data {string} and {string} and {string} and {string} and {short} and {short} and true and false")
    public void data_and_and_and_and_and_and_true_and_false(String brand, String model, String type, String licensePlate, short numberOfSeats, short numberOfChildSeats) {
       car.setBrand("Toyota");
       car.setModel("Corolla");
       car.setType("Sedan");
       car.setLicensePlate("XYZ1234");
       car.setNumberOfSeats( (short) 5);
       car.setNumberOfChildSeats((short) 2);       
       car.setFoldingRearSeat(true);
       car.setTowBar(false);
    }
    @When("I want to add a new car with this data")
    public void i_want_to_add_a_new_car_with_this_data() {
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
                    .bodyValue(car)
                    .exchange();
    }
    @Then("data of this car is returned \\{{string}: {string}, {string}: {string}, {string}: {string}, {string}: {string}, {string}: {short}, {string}: {short}, {string}: true, {string}: false}")
    public void data_of_this_car_is_returned_true_false(String key1, String value1, String key2, String value2, String key3, String value3, String key4, String value4, String key5, short value5, String key6, short value6, String key7, String value7) {
        String expectedJson = String.format("{\""+key1+"\": \""+value1+"\", \""+key2+"\": \""+value2+"\", \""+key3+"\": \""+value3+"\", \""+key4+"\": \""+value4+"\", \""+key5+"\": %d, \""+key6+"\": %d, \""+key7+"\": true, \""+value7+"\": false}", value5, value6);
        response.expectStatus()
                .isOk()
                .expectBody()
                .json(expectedJson);
    }

    @Given("In valid data {string} and {string} and {string} and {string} and {short} and {short} and true and false")
    public void in_valid_data_and_and_and_and_and_and_true_and_false(String string, String string2, String string3, String string4, short int1, short int2) {
       car.setBrand("");
       car.setModel("Corolla");
       car.setType("Sedan");
       car.setLicensePlate("XYZ1234");
       car.setNumberOfSeats( (short) 5);
       car.setNumberOfChildSeats((short) 2);       
       car.setFoldingRearSeat(true);
       car.setTowBar(false);
    }
    @When("I want to add a new car with this invalid data")
    public void i_want_to_add_a_new_car_with_this_invalid_data() {
        WebTestClient.ResponseSpec registerResponse = client.post()
        .uri("/register")
        .header("Content-Type", "application/json")
        .bodyValue("""
        {
            "email": "blas@bla.com",
            "password": "blabla!456",
            "firstName": "yaay",
            "lastName": "yipie",
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
                    .bodyValue(car)
                    .exchange();
    }
    @Then("error message \\{{string}: {string}} is returned")
    public void error_message_is_returned(String string, String string2) {
    String expectedJson = "{\""+string+"\": \""+string2+"\"}";
    response.expectStatus()
                .isBadRequest()
                .expectBody()
                .json(expectedJson);
    }









    // @Given("In valid type data {string} and {string} and {string} and {string} and {short} and {short} and true and false")
    // public void in_valid_type_data_and_and_and_and_and_and_true_and_false(String string, String string2, String string3, String string4, short int1, short int2) {
    //    car.setBrand("Toyota");
    //    car.setModel("Corolla");
    //    car.setType("");
    //    car.setLicensePlate("XYZ1234");
    //    car.setNumberOfSeats( (short) 5);
    //    car.setNumberOfChildSeats((short) 2);       
    //    car.setFoldingRearSeat(true);
    //    car.setTowBar(false);
    // }
    // @When("I want to add a new car with this invalid type data")
    // public void i_want_to_add_a_new_car_with_this_invalid_type_data() {
    //     WebTestClient.ResponseSpec registerResponse = client.post()
    //     .uri("/register")
    //     .header("Content-Type", "application/json")
    //     .bodyValue("""
    //     {
    //         "email": "blas@bla.com",
    //         "password": "blabla!456",
    //         "firstName": "yaay",
    //         "lastName": "yipie",
    //         "isRenter": false,
    //         "isOwner": true,
    //         "phoneNumber": "0987654321",
    //         "birthDate": "1988-12-12",
    //         "nationalRegisterNumber": "88.12.12-456.78",
    //         "licenseNumber": "0987654321"
    //     }
    //     """)
    //     .exchange()
    //     .expectStatus()
    //     .isOk();

    //     String responseBody = registerResponse.returnResult(String.class).getResponseBody().blockFirst();
    //     try {
    //         JsonNode jsonNode = objectMapper.readTree(responseBody);
    //         token = jsonNode.get("token").asText();
    //     } catch (Exception e) {
    //         System.out.println("Test");
    //     }

    //     response = client.post()
    //                 .uri("/car/add")
    //                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
    //                 .bodyValue(car)
    //                 .exchange();
    // }
    // @Then("error message \\{{string}: {string}} is returned")
    // public void type_error_message_is_returned(String string, String string2) {
    // String expectedJson = "{\""+string+"\": \""+string2+"\"}";
    // response.expectStatus()
    //             .isBadRequest()
    //             .expectBody()
    //             .json(expectedJson);
    // }


    @Given("In valid type data {string} and {string} and {string} and {string} and {int} and {int} and true and false")
    public void in_valid_type_data_and_and_and_and_and_and_true_and_false(String string, String string2, String string3, String string4, Integer int1, Integer int2) {
        car.setBrand("Toyota");
        car.setModel("Corolla");
        car.setType("");
        car.setLicensePlate("XYZ1234");
        car.setNumberOfSeats( (short) 5);
        car.setNumberOfChildSeats((short) 2);       
        car.setFoldingRearSeat(true);
        car.setTowBar(false);
    }

    @When("I want to add a new car with this invalid type data")
    public void i_want_to_add_a_new_car_with_this_invalid_type_data() {
        WebTestClient.ResponseSpec registerResponse = client.post()
        .uri("/register")
        .header("Content-Type", "application/json")
        .bodyValue("""
        {
            "email": "daad@dada.com",
            "password": "asassaassa!456",
            "firstName": "yaay",
            "lastName": "yipie",
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
                    .bodyValue(car)
                    .exchange();
    }

    @Then("type error message \\{{string}: {string}} is returned")
    public void type_error_message_is_returned(String string, String string2) {
        String expectedJson = "{\""+string+"\": \""+string2+"\"}";
        response.expectStatus()
                .isBadRequest()
                .expectBody()
                .json(expectedJson);
    
    }

    
}
