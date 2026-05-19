Feature: [C.U] Update a contract
  As a manager 
  I want to update some fields of a contract

  Scenario: [C.U.1] Update end date
    Given [C.U.1] an in-force fixed-term contract
    When [C.U.1] I update the end date of the contract
    Then [C.U.1] the end date is updated

  Scenario: [C.U.2] Update salary
    Given [C.U.2] an in-force contract
    When [C.U.2] I update the salary of the contract
    Then [C.U.2] the salary is updated

  Scenario: [C.U.3] Try to update a non existing contract  
    When [C.U.3] I try to update a non existing contract
    Then [C.U.3] a business error happens with an explaining message

  Scenario: [C.U.4] Try to update a contract terminated
    Given [C.U.4] a terminated contract
    When [C.U.4] I try to update that terminated contract
    Then [C.U.4] a business error happens with an explaining message

  Scenario: [C.U.5] Try to update a contract with wrong end date
    Given [C.U.5] an in-force fixed-term contract
    When [C.U.5] I try to update that with end date earlier than start date
    Then [C.U.5] a business error happens with an explaining message

  Scenario: [C.U.6] Try to update a contract in the while (wrong version)
    Given [C.U.6] an in-force contract
    When [C.U.6] I try to update a contract updated in the while
    Then [C.U.6] a business error happens with an explaining message

  Scenario: [C.U.7] Try to update a null contract
    When [C.U.7] I try to update a null contract
    Then [C.U.7] argument is rejected with an explaining message

  Scenario: [C.U.8] Try to update a contract with null id
    When [C.U.8] I try to update a contract with null id
    Then [C.U.8] argument is rejected with an explaining message

  Scenario Outline: [C.U.9] Try to update a contract with wrong id
    When [C.U.9] I try to update a contract with id <ident>
    Then [C.U.9] argument is rejected with an explaining message
    Examples: 
      | ident  |
      | "null" |
      | "  "   |
      | ""     |

  Scenario Outline: [C.U.10] Try to update a contract with wrong salary
    Given [C.U.10] an in-force contract
    When [C.U.10] I try to update a contract with wrong salary <money>
    Then [C.U.10] argument is rejected with an explaining message
    Examples: 
      | money  |
      |    0.0 |
      | -100.0 |