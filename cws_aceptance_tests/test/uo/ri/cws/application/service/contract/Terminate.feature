Feature: [C.T] Terminate a contract
  As a manager 
  If the mechanic changes it lobour state or it is fired we need terminate his/her contract

  Scenario: [C.T.1] Terminate a contract in force shorter than a year
    Given [C.T.1] an in-force contract shorter than a year with its payrolls
    When [C.T.1] I terminate that contract
    Then [C.T.1] there is no active contract for the mechanic
      And [C.T.1] end date of the terminated contract is set to end day in the month
      And [C.T.1] there is no settlement
      
  Scenario: [C.T.2] Terminate a contract in force larger than a year
    Given [C.T.2] an in-force contract longer than a year with its payrolls
    When [C.T.2] I terminate that contract
    Then [C.T.2] settlement is properlly calculated

  Scenario: [C.T.3] Try to terminate a terminated contract
    Given [C.T.3] a terminated contract 
    When [C.T.3] I try to terminate a terminated contract for mechanic <nif>
    Then [C.T.3] a business error happens with an explaining message
      
  Scenario: [C.T.4] Try to terminate a non existent contract
    When [C.T.4] I try to terminate a non existent contract
    Then [C.T.4] a business error happens with an explaining message

  Scenario Outline: [C.T.5] Try to terminate a contract with wrong id
    When [C.T.5] I try to terminate a contract with wrong <id>
    Then [C.T.5] argument is rejected with an explaining message
    Examples:
      | id     | 
      | "null" |
      | ""     |
      | "  "   |