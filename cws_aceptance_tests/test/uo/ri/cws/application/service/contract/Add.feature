Feature: [C.A] Add a contract for a mechanic
  As a Manager
  I want to hire a mechanic

  Scenario Outline: [C.A.1] Hire an existing mechanic with no previous contract in force
    Given [C.A.1] a mechanic with no contract
    When [C.A.1] I hire that mechanic with an annual <salary>
    Then [C.A.1] a contract in force for that mechanic is created
      And [C.A.1] start date is first day next month
    Examples:
        | salary   |
        | 12000.0  |
        | 18000.0  |
        | 24000.0  |
        | 30000.0  |
        | 36000.0  |
        | 60000.0  |
        | 90000.0  |
        | 320000.0 |

  Scenario: [C.A.2] Hire an existing mechanic with a previous contract in force shorter than a year
    Given [C.A.2] an already less-than-a-year hired mechanic
    When [C.A.2] I create a new contract for that mechanic
    Then [C.A.2] there is a new contract in force for that mechanic
      And [C.A.2] previous contract is terminated with no settlement

  Scenario: [C.A.3] Hire an existing mechanic with previous contract terminated
    Given [C.A.3] a mechanic with a previous contract terminated
    When [C.A.3] I hire that mechanic
    Then [C.A.3] a contract in force for that mechanic is created

  Scenario: [C.A.4] Try to add a contract for a non existing mechanic
    When [C.A.4] I try to hire a non existent mechanic
    Then [C.A.4] a business error happens with an explaining message

  Scenario: [C.A.5] Try to add a contract for a non existing contract type
    When [C.A.5] I try to hire a mechanic with non existent contract type
    Then [C.A.5] a business error happens with an explaining message

  Scenario: [C.A.6] Try to add a contract for a non existing professional group
    When [C.A.6] I try to hire a mechanic with non existent professional group
    Then [C.A.6] a business error happens with an explaining message

  Scenario: [C.A.7] Try to add a contract with end date earlier than start date
    When [C.A.7] I try to add a contract with end date not later than start date
    Then [C.A.7] a business error happens with an explaining message

  Scenario: [C.A.8] Try to add a null contract
    When [C.A.8] I try to add a null argument
    Then [C.A.8] argument is rejected with an explaining message

  Scenario: [C.A.11] Try to add a contract with null end date when mandatory
    When [C.A.11] I try to add a contract with null end date for FIXED_TERM contract type
    Then [C.A.11] argument is rejected with an explaining message

  Scenario Outline: [C.A.12] Try to add a contract with wrong nif
    When [C.A.12] I try to add a contract with wrong nif <nif>
    Then [C.A.12] argument is rejected with an explaining message
    Examples:
      | nif    |
      | "null" |
      | "   "  |
      | ""     |

  Scenario Outline: [C.A.13] Try to add a contract with wrong values
    When [C.A.13] I try to add a contract with wrong fields <type> <profGroup> <annualWage>
    Then [C.A.13] argument is rejected with an explaining message
    Examples:
      | type        | profGroup | annualWage |
      | "   "       | "I"       | 1000.0     |
      | ""          | "I"       | 1000.0     |
      | "PERMANENT" | " "       | 1000.0     |
      | "PERMANENT" | ""        | 1000.0     |
      | "PERMANENT" | "I"       | 0.0        |
      | "PERMANENT" | "I"       | -1000.0    |