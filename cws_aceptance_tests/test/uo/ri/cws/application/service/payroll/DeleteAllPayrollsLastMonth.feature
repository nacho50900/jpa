Feature: [P.Da] Delete all Payrolls previous month
  As a manager
  I want to remove all payrolls generated previous month, if any, due to some error

  Scenario: [P.Da.1] There is no payroll at all
    When [P.Da.1] I delete previous month payrolls
    Then [P.Da.1] there is no payroll

  Scenario: [P.Da.2] There are payrolls but not of previous month
    Given [P.Da.2] several payrolls of several mechanics not of the previous month
    When [P.Da.2] I delete previous month payrolls
    Then [P.Da.2] no payroll was deleted

  Scenario: [P.Da.3] There are payrolls but alll for the previous month
    Given [P.Da.3] several payrolls of several mechanics all for the previous month
    When [P.Da.3] I delete previous month payrolls
    Then [P.Da.3] all the payrolls are deleted

  Scenario: [P.Da.4] There are payrolls from several months ago including previous month
    Given [P.Da.4] several payrolls of several mechanics from several month ago including previous month
    When [P.Da.4] I delete previous month payrolls
    Then [P.Da.4] all previous month payrolls are deleted

  Scenario: [P.Da.5] Deleting payrolls twice in a row
    Given [P.Da.5] several payrolls of several mechanics from several month ago including previous month
    And [P.Da.5] I delete previous month payrolls
    When [P.Da.5] I (re)delete previous month payrolls
    Then [P.Da.5] no payroll was deleted