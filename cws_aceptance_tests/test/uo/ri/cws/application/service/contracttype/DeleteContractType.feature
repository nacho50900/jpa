Feature: [Ct.D] Delete a contract type
  As a Manager
  I need to delete a contract type. Perhaps, I typed something wrong

  Scenario: [Ct.D.1] Delete a contract type
    Given [Ct.D.1] a contract type with no contracts
    When [Ct.D.1] I delete that contract type
    Then [Ct.D.1] The contract is deleted

  Scenario: [Ct.D.2] Try to delete a non existent contract type
    When [Ct.D.2] I try to delete a non existent contract type
    Then [Ct.D.2] a business error happens with an explaining message

  Scenario: [Ct.D.3] Try to delete a contract type with contracts
    Given [Ct.D.3] a contract type with several contracts
    When [Ct.D.3] I try to delete that contract type
    Then [Ct.D.3] a business error happens with an explaining message

  Scenario Outline: [Ct.D.4] Try to delete a contract type with invalid argument
    When [Ct.D.4] I try to del a contract type with name <name>
    Then [Ct.D.4] argument is rejected with an explaining message
    Examples:
      | name  |
      | "null"|
      | ""    |
      | "   " |