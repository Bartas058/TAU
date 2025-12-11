# language: en
Feature: Searching for information on the PJATK website

    As a PJATK website user
    I want to search for information using the search bar
    So that I can find content related to a given phrase

    Scenario: PJATK Website – searching and opening the first result
        Given I open the PJATK homepage
        And I accept cookies - if visible
        When I search for the phrase "test" on the PJATK website
        Then I should see at least one search result
        And I open the first search result
