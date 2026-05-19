Feature: [P.G] Generate payrolls for mechanics last month
  As a Manager
  I want to generate payslips for my employees

  Scenario: [P.G.1] Generate payrolls but no mechanic
    When [P.G.1] I generate payrolls
    Then [P.G.1] zero payrolls are generated

  Scenario: [P.G.2] Generate payrolls for a mechanic with no active contract last month
    Given [P.G.2] a mechanic with several contracts but none active
    When [P.G.2] I generate payrolls
    Then [P.G.2] zero payrolls are generated

  Scenario Outline: [P.G.3] Generate payrolls for a mechanic in force last month
    Given [P.G.3] today is <today>
	  And [P.G.3] a professional group with triennium pay of 30.0 and productivity rate of 0.05
	  And [P.G.3] a contract in-force with <start> date, <annual> salary and its corresponding tax rate
	  And [P.G.3] having invoiced <invoiced> euros in workorders in the month prior
    When [P.G.3] I generate payrolls
    Then [P.G.3] one is generated with <date>, <monthly>, <extra>, <prod>, <tri>, <tax>, <nic>
	Examples:
	  | today       | date        | start       | annual   | invoiced | monthly   | extra   | prod  | tri   | tax      | nic     |
	  | "31/01/2026"| "31/12/2025"| "01/06/2025"| 12600.0  | 0.0      | 900.00    | 900.00  | 0.0   | 0.0   | 432.00   | 52.50   |
	  | "08/01/2022"| "31/12/2021"| "01/06/2009"| 14000.0  | 0.0      | 1000.00   | 1000.00 | 0.0   | 120.0 | 508.80   | 58.33   |
	  | "15/11/2025"| "31/10/2025"| "01/06/2025"| 15400.0  | 5000.0   | 1100.00   | 0.00    | 250.0 | 0.0   | 324.00   | 64.17   |
	  | "01/11/2021"| "31/10/2021"| "01/06/2000"| 40000.0  | 7500.0   | 2857.14   | 0.00    | 375.0 | 210.0 | 1273.59  | 166.67  |
	  | "31/01/2026"| "31/12/2025"| "01/06/2025"| 70000.0  | 0.0      | 5000.00   | 5000.00 | 0.0   | 0.0   | 4500.00  | 291.67  |
	  | "10/01/2022"| "31/12/2021"| "01/06/2010"| 19600.0  | 0.0      | 1400.00   | 1400.00 | 0.0   | 90.0  | 693.60   | 81.67   |
	  | "30/09/2025"| "31/08/2025"| "01/06/2025"| 21000.0  | 6700.0   | 1500.00   | 0.00    | 335.0 | 0.0   | 550.50   | 87.50   |
	  | "01/09/2021"| "31/08/2021"| "01/06/2012"| 350000.0 | 8000.0   | 25000.00  | 0.00    | 400.0 | 90.0  | 11980.30 | 1458.33 |
	  | "30/07/2025"| "30/06/2025"| "01/06/2025"| 21000.0  | 6700.0   | 1500.00   | 1500.00 | 335.0 | 0.0   | 1000.50  | 87.50   |
	  | "01/07/2021"| "30/06/2021"| "01/06/1995"| 22400.0  | 8000.0   | 1600.00   | 1600.00 | 400.0 | 240.0 | 1152.00  | 93.33   |

  Scenario: [P.G.4] Generate payrolls only for mechanics with an active contract.
    Given [P.G.4] today is "2025-08-06"
    And [P.G.4] the following relation of mechanics with a contract
	  | nif   | state      | startDate  | endDate    |
	  | NIF1  | TERMINATED | 2021-01-01 | 2021-05-01 | # no longer an employee
	  | NIF2  | TERMINATED | 2022-01-01 | 2023-05-31 | # first contract finished
	  | NIF2  | TERMINATED | 2023-06-01 | 2023-12-31 | # renewed and finished
	  | NIF2  | IN_FORCE   | 2024-01-01 | 2021-12-01 | # renewed and in force
	  | NIF3  | IN_FORCE   | 2023-06-01 |            | # in force
    When [P.G.4] I generate payrolls
    Then [P.G.4] two payrolls are generated
    
   Scenario: [P.G.5] Generate payrolls twice in the month has no effect
     Given [P.G.5] several already generated payrolls for this month
     When [P.G.5] I generate payrolls again
     Then [P.G.5] no new payrolls are generated
