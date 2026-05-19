Feature: [C.F] Find contracts
  As a manager I want to find an specific contract by id,
  all contracts for a mechanic by mechanic dni
  and also all contracts

  # Find all contracts
  Scenario: [C.F.1] Find all contracts when there are none
    When [C.F.1] I search for all contracts
    Then [C.F.1] List of contracts summary is empty

  Scenario: [C.F.2] Find all contracts when there are some
    Given [C.F.2] several contracts in the system
    When [C.F.2] I search for all contracts
    Then [C.F.2] All contract summaries are found

  # Find contract by id
  Scenario: [C.F.3] Find by id an existent contract in force
    Given [C.F.3] an in-force contract
    When [C.F.3] I search for that contract
    Then [C.F.3] the contract is found

  Scenario: [C.F.4] Find by id an existent contract terminated
    Given [C.F.4] an terminated contract
    When [C.F.4] I search for that contract
    Then [C.F.4] the contract is found

  Scenario: [C.F.5] Find by id a non existent contract
    When [C.F.5] I search a non existing contract id
    Then [C.F.5] nothing is found

  Scenario: [C.F.6] Try to find a contract with null id
    When [C.F.6] I try to find a contract with null id
    Then [C.F.6] argument is rejected with an explaining message

  Scenario: [C.F.7] Try to find a contract for a mechanic with null arg
    When [C.F.7] I try to find a contract for a mechanic with null id
    Then [C.F.7] argument is rejected with an explaining message

  # Find contracts by mechanic id
  Scenario: [C.F.8] Find contracts by mechanic id for a non existent mechanic
    When [C.F.8] I search contracts for a non existent mechanic
    Then [C.F.8] List of contracts summary is empty

  Scenario: [C.F.9] Find contracts by mechanic id for an existent mechanic with no contracts
    Given [C.F.9] a mechanic with no contracts
    When [C.F.9] I search contracts for that mechanic
    Then [C.F.9] List of contracts summary is empty

  Scenario: [C.F.10] Find contracts by mechanic ID for an existing mechanic with no active contracts and several terminated contracts with no payrolls.
    Given [C.F.10] a mechanic with several contracts terminated
    When [C.F.10] I search contracts for that mechanic
    Then [C.F.10] All contract summaries are found

  Scenario: [C.F.11] Find contracts by mechanic ID for an existing mechanic with one active contract and several payrolls.
    Given [C.F.11] a mechanic with one in-force contract and several payrolls
    When [C.F.11] I search contracts for that mechanic
    Then [C.F.11] All contract summaries are found

  Scenario: [C.F.12] Find contracts by mechanic id for an existent mechanic with several contracts terminated and one contract in force with several payrolls
    Given [C.F.12] a mechanic with several contracts terminated and one in force all with payrolls
    When [C.F.12] I search contracts for that mechanic
    Then [C.F.12] All contract summaries are found