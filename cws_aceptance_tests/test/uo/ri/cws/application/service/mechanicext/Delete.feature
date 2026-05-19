
Feature: [M.D] Delete a mechanic
  As a Manager
  I want to delete a mechanic in case they have not been used
  Because we need to keep the system clean

  Scenario: [M.D.7] Try to delete a mechanic with a terminated contract
    Given [M.D.7] a mechanic with a terminated contract
    When [M.D.7] I try to delete the mechanic
    Then [M.D.y] a business error happens with an explaining message
