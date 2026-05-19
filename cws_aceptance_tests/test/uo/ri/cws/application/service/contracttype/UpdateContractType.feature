Feature: [Ct.U] Update a contract type
  I want to use this template for my feature file

  Scenario: [Ct.U.1] Update an existent contract type
    Given [Ct.U.1] a registered contract type
    When [Ct.U.1] I update contract type compensatiom days
    Then [Ct.U.1] only the compensation are updated

  Scenario: [Ct.U.2] Try to update a non existent contract type
    When [Ct.U.2] I try to update a non existent contract type
    Then [Ct.U.2] a business error happens with an explaining message

  Scenario: [Ct.U.3] Try to update a contract type with null arg
    When [Ct.U.3] I try to update a contract type with null arg
    Then [Ct.U.3] argument is rejected with an explaining message

  Scenario: [Ct.U.4] Try to update a contract type with negative days
    When [Ct.U.4] I try to update a contract type with negative days
    Then [Ct.U.4] argument is rejected with an explaining message

  Scenario: [Ct.U.5] Try to update a contract type in the while (wrong version)
    Given [Ct.U.5] a registered contract type
    When [Ct.U.5] I try to update a contract type updated in the while
    Then [Ct.U.5] a business error happens with an explaining message

  Scenario Outline: [Ct.U.6] Try to update a contract type with empty name
    When [Ct.U.6] I try to update a contract type with <name> name
    Then [Ct.U.6] argument is rejected with an explaining message
    Examples:
      | name   |
      | "null" |
      | ""     |
      | "   "  |