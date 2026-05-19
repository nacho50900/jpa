Feature: [P.Fs] Find payrolls all scenarios
  As a manager I want to find information about payrolls

# Find all payrolls
  Scenario: [P.Fs.1] Find payrolls when there is none
    When [P.Fs.1] I search for all payrolls
    Then [P.Fs.1] the result is empty

  Scenario: [P.Fs.2] Find payrolls when there are some
    Given [P.Fs.2] several payrolls already created
    When [P.Fs.2] I search all payrolls
    Then [P.Fs.2] all the payroll summaries are returned
       And [P.Fs.2] all the summaries are right

# Find payrolls by mechanic
  Scenario: [P.Fs.3] Find payrolls for a mechanic with one payroll
    Given [P.Fs.3] a mechanic with one payroll
    When [P.Fs.3] I search payrolls by that mechanic
    Then [P.Fs.3] the right payroll summary for that mechanic is found

  Scenario: [P.Fs.4] Find payrolls for a mechanic with several payrolls
    Given [P.Fs.4] a mechanic with several payrolls
    When [P.Fs.4] I search payrolls by that mechanic
    Then [P.Fs.4] all the payroll summaries for that mechanic are found

  Scenario: [P.Fs.5] Find payrolls for a mechanic with no payroll
    Given [P.Fs.5] a mechanic with no payrolls
    When [P.Fs.5] I search payrolls by that mechanic
    Then [P.Fs.5] the result is empty

  Scenario: [P.Fs.6] Find payrolls for a non existent mechanic
    When [P.Fs.6] I try to find payrolls for a non existent mechanic
    Then [P.Fs.6] a business error happens with an explaining message

  Scenario Outline: [P.Fs.7] Try to find a payroll with invalid mechanic id
    When [P.Fs.7] I try to find payrolls with an invalid mechanic <id>
    Then [P.Fs.7] argument is rejected with an explaining message
    Examples:
      | id     |
      | "null" |
      | ""     |
      | " "    |

# Find payrolls by professional group
  Scenario: [P.Fs.8] Find payrolls for a professional group with one payroll
    Given [P.Fs.8] a payroll from a contract with a professional group
    When [P.Fs.8] I search payrolls by that professional group
    Then [P.Fs.8] the right payroll summary for that professional group is found

  Scenario: [P.Fs.9] Find payrolls for a professional group with several payrolls
  	Given [P.Fs.9] some payrolls of some contracts of some professional groups
    When [P.Fs.9] I search payrolls for one of those professional groups
    Then [P.Fs.9] all payroll summaries for that professional group are found

  Scenario: [P.Fs.10] Find payrolls for a professional group with no payroll
    Given [P.Fs.10] a professional group with no payrolls
    When [P.Fs.10] I search payrolls for that professional group
    Then [P.Fs.10] the result is empty

  Scenario: [P.Fs.11] Find payrolls for a non existent professional group
    When [P.Fs.11] I try to find payrolls with a non existent professional group name
    Then [P.Fs.11] a business error happens with an explaining message

  Scenario Outline: [P.Fs.12] Try to find a payroll with invalid professional group name
    When [P.Fs.12] I try to find payrolls with invalid professional group <name>
    Then [P.Fs.12] argument is rejected with an explaining message
    Examples:
      | name   |
      | "null" |
      | ""     |
      | "   "  |