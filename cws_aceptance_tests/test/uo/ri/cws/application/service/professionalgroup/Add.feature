Feature: [Pg.A] Add a new professional group
	As a Manager
	I want to add a new professional group due to changes in labour laws
	 
	Scenario: [Pg.A.1] Add a new professional group
		When [Pg.A.1] I register a new professional group
		Then [Pg.A.1] The professional group is added

	Scenario: [Pg.A.2] Try to add a repeated professional group
		Given [Pg.A.2] a professional group 
		When [Pg.A.2] I try to add a professional group with the same name
		Then [Pg.A.2] a business error happens with an explaining message

	Scenario: [Pg.A.3] Try to add a professional group with null arg
		When [Pg.A.3] I try to add a professional group with null argument
		Then [Pg.A.3] argument is rejected with an explaining message

	Scenario: [Pg.A.4] Try to add a professional group with negative triennium
		When [Pg.A.4] I try to add a professional group with negative triennium
		Then [Pg.A.4] argument is rejected with an explaining message
  
	Scenario: [Pg.A.5] Try to add a professional group with negative productivity plus
		When [Pg.A.5] I try to add a professional group with negative productivity plus
		Then [Pg.A.5] argument is rejected with an explaining message

	Scenario Outline: [Pg.A.6] Try to add a professional group with empty name
		When [Pg.A.6] I try to add a professional group with name <name>
		Then [Pg.A.6] argument is rejected with an explaining message
		Examples:
			| name   | 
			| "null" |
			| ""     |
			| "   "  |