Feature: Category

  Scenario Outline: Add new category, find added category and delete added category
    Given I open web browser for category test
    When I navigate to categories.html page
    And I click on Add category button
    And I provide category name as "<categoryName>"
    And I click on submit button
    Then I find "<categoryName>"
    And I delete "<categoryName>"

    Examples:
      | categoryName |
      | testCategory |