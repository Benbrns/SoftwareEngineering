package be.ucll.se.groep02backend.demo.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import be.ucll.se.groep02backend.demo.model.domain.Greeting;

public interface GreetingRepository extends JpaRepository<Greeting, Long>{
    public List<Greeting> findAll();
}
