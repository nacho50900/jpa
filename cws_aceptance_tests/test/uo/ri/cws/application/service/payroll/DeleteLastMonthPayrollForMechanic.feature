Feature: [P.D] Delete previous month payroll for a mechanic
  As a Manager
  I want to delete previous month payroll for a mechanic
  because, sometimes, payrolls are generated with errors

  Scenario: [P.D.1] Delete previous month payroll for a mechanic with no payroll
    Given [P.D.1] a mechanic with a contract in force but still no payrolls
    When [P.D.1] I delete previous month payroll for that mechanic
    Then [P.D.1] no payroll is deleted

  Scenario: [P.D.2] Delete previous month payroll for a mechanic with only one payroll
    Given [P.D.2] a mechanic with a contract in force and only one payroll previous month
    When [P.D.2] I delete previous month payroll for that mechanic
    Then [P.D.2] the previous month payroll is removed

  Scenario: [P.D.3] Delete previous month payroll for a mechanic with several payroll none previous month
    Given [P.D.3] a mechanic with a contract in force and several payrolls none previous month
    When [P.D.3] I delete previous month payroll for that mechanic
    Then [P.D.3] no payroll is removed

  Scenario: [P.D.4] Delete previous month payroll for a mechanic with several payroll including previous month
    Given [P.D.4] a mechanic with a contract in force and several payrolls one previous month
    When [P.D.4] I delete previous month payroll for that mechanic
    Then [P.D.4] the previous month payroll is removed

  Scenario: [P.D.5] Try to delete previous month payroll for a non existent mechanic
    When [P.D.5] I try to delete previous month payroll for a non existent mechanic
    Then [P.D.5] a business error happens with an explaining message

  Scenario Outline: [P.D.6] Try to delete last payroll with invalid mechanic dni
    When [P.D.6] I try to delete with wrong mechanic <dni> argument
    Then [P.D.6] argument is rejected with an explaining message
    Examples: 
      | dni    |
      | "null" |
      | ""     |
      | "   "  |