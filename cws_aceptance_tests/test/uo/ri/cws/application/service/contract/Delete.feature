Feature: [C.D] Delete a contract 
  As a Manager
  I want to delete a contract (I wrote sth wrong and I want to delete it and then try to do it right)
 
  Scenario: [C.D.1] Delete a contract
    Given [C.D.1] a contract for a mechanic with no payrolls neither interventions
    When [C.D.1] I delete the contract for the mechanic
    Then [C.D.1] This contract does not exist any more

  Scenario: [C.D.2] Try to delete a contract for a mechanic with payrolls
    Given [C.D.2] a contract with payrolls
    When [C.D.2] I try to delete that contract
    Then [C.D.2] a business error happens with an explaining message
      
  Scenario: [C.D.3] Try to delete a contract for a mechanic with interventions during the contract
    Given [C.D.3] a contract for a mechanic with interventions during the contract
    When [C.D.3] I try to delete that contract
    Then [C.D.3] a business error happens with an explaining message 

  Scenario: [C.D.4] Try to delete a non existing contract
    When [C.D.4] I try to delete a non existing contract
    Then [C.D.4] a business error happens with an explaining message
            
  Scenario: [C.D.5] Try to delete a null contract id
    When [C.D.5] I try to delete a null contract id
    Then [C.D.5] argument is rejected with an explaining message