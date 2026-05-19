Feature: [Pg.D] Delete a professional group
	As a Manager
	I need to delete a professional group. Perhaps, I typed something wrong
	 
	Scenario: [Pg.D.1] Delete a professional group with no contracts
		Given [Pg.D.1] a professional group with no contracts
		When [Pg.D.1] I delete that professional group
		Then [Pg.D.1] The professional group is removed from the system

	Scenario Outline: [Pg.D.2] Try to del a professional group with empty argument
		When [Pg.D.2] I try to del a professional group with wrong name <name>
		Then [Pg.D.2] argument is rejected with an explaining message
		Examples:
			| name   |
			| "null" |
			| ""     |
			| "   "  |

	Scenario: [Pg.D.3] Try to del a non existent professional group 
		When [Pg.D.3] I try to del a non existent professional group
		Then [Pg.D.3] a business error happens with an explaining message

	Scenario: [Pg.D.4] Try to del a professional group involved in some contract 
		Given [Pg.D.4] a professional group with some contracts
		When [Pg.D.4] I try to delete that professional group 
		Then [Pg.D.4] a business error happens with an explaining message