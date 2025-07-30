Feature: Getting animals from the get API

  Scenario: Validation of positive response code for Get Animals from the application
    Given I am an authenticated user
    When I hit the get animals api url
    Then I get 200 as the response code

  Scenario: Validation of positive response body for Get Animals from the application
    Given I am an authenticated user
    When I hit the get animals api url
    Then I get animals in the response body of the api

  Scenario: Validation of negative response code for Get Animals from the application
    Given I am an unauthenticated user
    Then I get 400 as the response code

  Scenario: Validation of negative response body for Get Animals from the application
    Given I hit the get animals api url without access token
    Then I get 401 as the response code