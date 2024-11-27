Feature: get rentals
    Scenario Outline:  get all rentals
        Given some rentals
        When the user requests all rentals
        Then all rents are returned
        
