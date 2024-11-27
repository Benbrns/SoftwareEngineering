Feature: Get all Notifications
    Scenario: Getting all notifications
        Given there are some notifications
        When I want to get all notifications
        Then the data of all notifications is returned in json format
