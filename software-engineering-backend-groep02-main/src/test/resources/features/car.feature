Feature: cars
    Scenario Outline:  add car with valid data
        Given data <brand> and <model> and <type> and <licensePlate> and <numberOfSeats> and <numberOfChildSeats> and <foldingRearSeat> and <towBar>
        When I want to add a new car with this data
        Then data of this car is returned <data>

    Examples:
        |   brand   |   model   |   type    |   licensePlate    |   numberOfSeats   |   numberOfChildSeats |    foldingRearSeat |   towBar  | data  |
        |   "Toyota" |   "Corolla"   |   "Sedan" |   "XYZ1234"   |   5   |   2   |   true    |   false   | {"brand": "Toyota", "model": "Corolla", "type": "Sedan", "licensePlate": "XYZ1234", "numberOfSeats": 5, "numberOfChildSeats": 2, "foldingRearSeat": true, "towBar": false}    |
        
    Scenario Outline: add car with invalid data
        Given In valid data <brand> and <model> and <type> and <licensePlate> and <numberOfSeats> and <numberOfChildSeats> and <foldingRearSeat> and <towBar>
        When I want to add a new car with this invalid data
        Then error message {"brand": "Brand is required"} is returned

    Examples:
        |   brand   |   model   |   type    |   licensePlate    |   numberOfSeats   |   numberOfChildSeats |    foldingRearSeat |   towBar  | data  |
        |   "" |   "Corolla"   |   "Sedan" |   "XYZ1234"   |   5   |   2   |   true    |   false   |{"brand": "Brand is required"}    |

    Scenario Outline: add car with invalid type data
        Given In valid type data <brand> and <model> and <type> and <licensePlate> and <numberOfSeats> and <numberOfChildSeats> and <foldingRearSeat> and <towBar>
        When I want to add a new car with this invalid type data
        Then type error message {"type": "Type is required"} is returned

    Examples:
        |   brand   |   model   |   type    |   licensePlate    |   numberOfSeats   |   numberOfChildSeats |    foldingRearSeat |   towBar  | data  |
        |   "Toyota" |   "Corolla"   |   "" |   "XYZ1234"   |   5   |   2   |   true    |   false   |{"brand": "Brand is required"}    |
      