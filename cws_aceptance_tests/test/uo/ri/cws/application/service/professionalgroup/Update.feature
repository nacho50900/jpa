Feature: [Pg.U] Update a professional group
    As a Manager
    I need to update a professional group due to changes in labour laws

  Scenario: [Pg.U.1] Update a professional group
	Given [Pg.U.1] a registered professional group
	When [Pg.U.1] I update that professional group with new triennium and productivity 
	Then [Pg.U.1] Triennium and productivity are updated
 
  Scenario: [Pg.U.2] Try to update a non existent professional group
   	When [Pg.U.2] I try to update a non existent professional group
    Then [Pg.U.2] a business error happens with an explaining message

  Scenario: [Pg.U.3] Try to update a professional group in the while (wrong version)
    Given [Pg.U.3] a registered professional group
    When [Pg.U.3] I try to update that professional group updated in the while
    Then [Pg.U.3] a business error happens with an explaining message

  Scenario: [Pg.U.4] Try to update a professional group with negative triennium
    Given [Pg.U.4] a registered professional group
    When [Pg.U.4] I try to update a professional group with negative triennium
	Then [Pg.U.4] argument is rejected with an explaining message
  
  Scenario: [Pg.U.5] Try to update a professional group with negative productivity plus
    Given [Pg.U.5] a registered professional group
    When [Pg.U.5] I try to update a professional group with negative productivity plus
	Then [Pg.U.5] argument is rejected with an explaining message
    	
 Scenario: [Pg.U.6] Try to update a professional group with null arg
    When [Pg.U.6] I try to update a professional group with null arg
    Then [Pg.U.6] argument is rejected with an explaining message
  	  
  Scenario Outline: [Pg.U.7] Try to update a professional group with empty name
    When [Pg.U.7] I try to update a professional group with name <name>
	Then [Pg.U.7] argument is rejected with an explaining message
	Examples:
	|  name  |
	| "null" |
	| ""     |
	| "   "  |