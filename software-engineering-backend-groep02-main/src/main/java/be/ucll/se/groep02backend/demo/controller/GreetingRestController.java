package be.ucll.se.groep02backend.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import be.ucll.se.groep02backend.demo.model.domain.Greeting;
import be.ucll.se.groep02backend.demo.service.GreetingService;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@Tag(name = "Greeting")
@RequestMapping("/hello")
public class GreetingRestController {
    @Autowired
    private GreetingService greetingService;

    @GetMapping
    public List<Greeting> getGreeting() {
        return greetingService.findAll();
    }
}
