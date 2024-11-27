package be.ucll.se.groep02backend.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import be.ucll.se.groep02backend.demo.model.domain.Greeting;
import be.ucll.se.groep02backend.demo.repo.GreetingRepository;
import jakarta.annotation.PostConstruct;

@Component
public class InitGreetings {

    @Autowired
    private GreetingRepository greetingRepository;

    @PostConstruct
    public void insertGreeting() {
        greetingRepository.save(new Greeting("Hello world!"));
    }
}