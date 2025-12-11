# language: en
Feature: Adding a product to the PJATK store cart

    As a PJATK store customer
    I want to search for a product and add it to the cart
    So that I can verify that the purchasing process works correctly

    Scenario: PJATK Store – searching, opening a product and adding it to the cart
        Given I open the PJATK store homepage
        When I search for the product "karafka" in the PJATK store
        Then I should see at least one search result in the store
        When I open the first product from the search results in the store
        And I add the product to the cart
        And I open the cart page
        Then the cart should contain exactly 1 product
