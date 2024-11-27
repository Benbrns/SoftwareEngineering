package be.ucll.se.groep02backend.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.SpringFactory;

import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "be.ucll.se.groep02backend.bdd.steps",
    plugin = {"pretty", "json:target/cucumber-report/cucumber.json", "html:target/cucumber-report/cucumber-pretty.html"},
    objectFactory = SpringFactory.class
)
public class CucumberTest {
}