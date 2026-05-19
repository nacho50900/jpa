Feature: [Ct.F] Find contract type
  
  Scenario: [Ct.F.1] Find a non existent contract type
    When [Ct.F.1] I search a non existent contract type
    Then [Ct.F.1] contract type is not found

  Scenario: [Ct.F.2] Find an existent contract type
    Given [Ct.F.2] a contract type with a name
    When [Ct.F.2] I search a contract type by that name
    Then [Ct.F.2] contract type is found

  Scenario Outline: [Ct.F.3] Try to find a contract type with wrong name
    When [Ct.F.3] I try to find a contract type with name <arg>
    Then [Ct.F.3] argument is rejected with an explaining message
    Examples: 
      | arg  |
      | null |
      | "  " | 
      | ""   |