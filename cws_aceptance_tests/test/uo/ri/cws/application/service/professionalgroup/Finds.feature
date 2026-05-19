Feature: [Pg.F] Find professional groups
  As a manager 
  I want to find professional groups

  Scenario: [Pg.F.1] Find all existent professional groups
    Given [Pg.F.1] Several registered professional groups 
    When [Pg.F.1] I find for all professional groups
    Then [Pg.F.1] all professional groups are found

  Scenario: [Pg.F.2] Find an existent professional group
    Given [Pg.F.2] Several registered professional groups 
    When [Pg.F.2] I find by name for a specific existing professional group
    Then [Pg.F.2] the professional group is found
        	    
  Scenario: [Pg.F.3] Find a non existent professional group
    When [Pg.F.3] I search a non existing professional group
    Then [Pg.F.3] professional group is not found
        	    
  Scenario Outline: [Pg.F.4] Try to find a professional group with wrong arg
    When [Pg.F.4] I try to find a professional group with name <arg>
    Then [Pg.F.4] argument is rejected with an explaining message
    Examples: 
      | arg    |
      | "null" |
      | "  "   | 
      | ""     |