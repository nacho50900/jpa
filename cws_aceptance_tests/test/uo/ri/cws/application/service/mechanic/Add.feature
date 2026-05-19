
Feature: [M.A] Add a mechanic
  As a Manager
  I want to register a mechani
  Because we need a new worker

  Scenario: [M.A.1] Add a non existing mechanic
    Given [M.A.1] a new non existing mechanic
    When [M.A.1] I add a that mechanic
    Then [M.A.1] the mechanic results added to the system

  Scenario: [M.A.2] Try to add a mechanic with a repeated nif
    Given [M.A.2] a registered mechanic
    When [M.A.2] I try to add a new mechanic with same nif
    Then [M.A.2] a business error happens with an explaining message

  Scenario: [M.A.3] Try to add a mechanic with null argument
    When [M.A.3] I try to add a new mechanic with null argument
    Then [M.A.3] argument is rejected with an explaining message

  Scenario: [M.A.4] Try to add a mechanic with null nif
    When [M.A.4] I try to add a new mechanic with null nif
    Then [M.A.4] argument is rejected with an explaining message

  Scenario: [M.A.5] Try to add a mechanic with invalid nif
    When [M.A.5] I try to add a new mechanic with <nif>, <name>, <surname>
    Then [M.A.5] argument is rejected with an explaining message
    Examples:
      | nif   | name    | surname    |
      | ""    | "name1" | "surname1" |
      | "   " | "name2" | "surname2" |
