Feature: [M.U] Update a mechanic
  As a Manager
  I want to update a mechanic

  Scenario: [M.U.1] Update an existing mechanic
    Given [M.U.1] a mechanic
    When [M.U.1] I update the mechanic
    Then [M.U.1] the mechanic results updated for fields name and surname
    And [M.U.1] mechanic version increases

  Scenario: [M.U.2] Try to update a non existing mechanic
    When [M.U.2] I try to update a non existing mechanic
    Then [M.U.2] a business error happens with an explaining message

  Scenario: [M.U.3] Try to update a mechanic updated in the while (wrong version)
    Given [M.U.3] a mechanic
    When [M.U.3] I try to update a mechanic updated in the while
    Then [M.U.3] a business error happens with an explaining message

  Scenario: [M.U.4] Try to update a mechanic with null name
    Given [M.U.4] a mechanic
    When [M.U.4] I try to update a mechanic with null name
    Then [M.U.4] argument is rejected with an explaining message

  Scenario: [M.U.5] Try to update a mechanic with null surname
    Given [M.U.5] a mechanic
    When [M.U.5] I try to update a mechanic with null surname
    Then [M.U.5] argument is rejected with an explaining message

  Scenario: [M.U.6] Try to update a mechanic with null nif
    Given [M.U.6] a mechanic
    When [M.U.6] I try to update a mechanic with null nif
    Then [M.U.6] argument is rejected with an explaining message

  Scenario Outline: [M.U.7] Try to update with wrong parameters
    Given [M.U.7] a mechanic
    When [M.U.7] I try to update the mechanic to <name>, <surname> and <nif>
    Then [M.U.7] argument is rejected with an explaining message
    Examples: 
      | nif            | name        | surname    |
      | "existing nif" | "Mechanic1" | ""         |
      | "existing nif" | "Mechanic1" | "     "    |
      | "existing nif" | ""          | "surname1" |
      | "existing nif" | "         " | "surname1" |
