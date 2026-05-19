Feature: [M.Fif] Find all in focrce mechanics
  As a Manager
  I need to know data about in force mechanics

  Scenario: [M.Fif.1] Find all existing mechanic with contracts in force
    Given [M.Fif.1] a relation of mechanics with no inforce contracts
    And [M.Fif.1] another relation of mechanics with inforce contracts
    When [M.Fif.1] we find for mechanics with contract in force
    Then [M.Fif.1] we get only the inforce mechanics 

  Scenario: [M.Fif.2] FWhen all the mechanics are inforce we get all of them
    Given [M.Fif.2] a relation of mechanics with inforce contracts
    When [M.Fif.2] we find for mechanics with contract in force
    Then [M.Fif.2] we get all the mechanics
  
  Scenario: [M.Fif.3] When no mechanics are inforce we get none of them
    Given [M.Fif.3] a relation of mechanics with no inforce contracts
    When [M.Fif.3] we find for mechanics with contract in force
    Then [M.Fif.3] we get no mechanics