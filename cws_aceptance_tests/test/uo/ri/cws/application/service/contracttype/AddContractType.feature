Feature: [Ct.A] Add a new contract type
  As a Manager
  I want to add a contract type group due to changes in labour laws

  Scenario: [Ct.A.1] Add a new contract type
    When [Ct.A.1] I register a new contract type
    Then [Ct.A.1] The contract type is added

  Scenario: [Ct.A.2] Try to add a repeated contract type
    Given [Ct.A.2] a registered contract type
    When [Ct.A.2] I try to add another contract type with the same name
    Then [Ct.A.2] a business error happens with an explaining message

  Scenario: [Ct.A.3] Try to add a contract type with null arg
    When [Ct.A.3] I try to add a contract type with null argument
    Then [Ct.A.3] argument is rejected with an explaining message

  Scenario: [Ct.A.4] Try to add a contract type with null name
    When [Ct.A.4] I try to add a contract type with null name
    Then [Ct.A.4] argument is rejected with an explaining message

  Scenario: [Ct.A.5] Try to add a contract type with negative compensation days
    When [Ct.A.5] I try to add a contract type with negative compensation days
    Then [Ct.A.5] argument is rejected with an explaining message

  Scenario Outline: [Ct.A.6] Try to add a contract type with empty name
    When [Ct.A.6] I try to add a contract type with <name>
    Then [Ct.A.6] argument is rejected with an explaining message
    Examples:
      | name   |
      | "null" |
      | ""     |
      | "   "  |