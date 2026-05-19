Feature: [P.F] Find payrolls 
  As a manager I want to find information about payrolls

  Scenario: [P.F.1] Find an existent payroll
    Given [P.F.1] a payroll
    When [P.F.1] I search payroll details for that payroll
    Then [P.F.1] that payroll is returned

  Scenario: [P.F.2] Find a non existent payroll
    When [P.F.2] I find a payroll with a non existent id
    Then [P.F.2] nothing is found

  Scenario Outline: [P.F.3] Try to find a payroll with invalid id
    When [P.F.3] I try to find payroll with and invalid <id>
    Then [P.F.3] argument is rejected with an explaining message
    Examples:
      | id     |
      | "null" |
      | ""     |
      | " "    |
      
  Scenario: [P.F.4] Find an existent payroll when several
    Given [P.F.4] several payrolls
    When [P.F.4] I search payroll details for that payroll
    Then [P.F.4] that payroll is returned
