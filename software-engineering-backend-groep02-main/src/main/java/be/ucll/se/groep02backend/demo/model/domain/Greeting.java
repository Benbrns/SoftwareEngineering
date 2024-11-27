package be.ucll.se.groep02backend.demo.model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(name= "greetings")
public class Greeting {
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Id
    public long id;
    private String text;

    public Greeting() {}

    public Greeting(String text) {
        this.text=text;
    }

    public String getText() {
        return this.text;
    }
}