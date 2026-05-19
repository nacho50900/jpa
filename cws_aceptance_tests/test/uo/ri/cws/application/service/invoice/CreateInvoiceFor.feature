Feature: [I.C] Create one invoice for one or multiple jobs
  As a Cashier
  I want to create an invoice for several jobs done for a single client 
  Because I want to get paid

  Scenario: [I.C.1] Create one invoice for an existing workorder
    Given [I.C.1] a client registered with a vehicle and one finished workorder
    When [I.C.1] I create an invoice for the workorders
    Then [I.C.1] an invoice is created
    And [I.C.1] the workorder is marked as INVOICED

  Scenario: [I.C.2] Create one invoice for multiple existing workorders
    Given [I.C.2] a client registered with a vehicle and a list of several finished workorders
    When [I.C.2] I create an invoice for the workorders
    Then [I.C.2] an invoice is created
    And [I.C.2] the workorders are marked as INVOICED

  Scenario: [I.C.3] Trying to create one invoice there is one non existing workorder
    Given [I.C.3] a client registered with a vehicle and a list of several finished workorders
    When [I.C.3] I try to create an invoice including one non existent workorder id
    Then [I.C.3] a business error happens with an explaining message

  Scenario: [I.C.4] Trying to create one invoice there is one workorder ASSIGNED
    Given [I.C.4] a client registered with a vehicle and a list of several finished workorder
    And [I.C.4] one ASSIGNED workorder
    When [I.C.4] I try to create an invoice
    Then [I.C.4] a business error happens with an explaining message

  Scenario: [I.C.5] Trying to create one invoice there is one workorder OPEN
    Given [I.C.5] a client registered with a vehicle and a list of several finished workorder
    And [I.C.5] one OPEN workorder
    When [I.C.5] I try to create an invoice
    Then [I.C.5] a business error happens with an explaining message

  Scenario: [I.C.6] Trying to create one invoice but one workorder is INVOICED
    Given [I.C.6] a client registered with a vehicle and a list of several finished workorder
    And [I.C.6] one INVOICED workorder
    When [I.C.6] I try to create an invoice
    Then [I.C.6] a business error happens with an explaining message

  Scenario: [I.C.7] Trying to create one invoice for a null argument
    When [I.C.7] I try to create an invoice for a null list of workorders
    Then [I.C.7] argument is rejected with an explaining message

  Scenario: [I.C.8] Trying to create one invoice for an empty argument
    When [I.C.8] I try to create an invoice for an empty list of workorders
    Then [I.C.8] argument is rejected with an explaining message

  Scenario: [I.C.9] Trying to create one invoice and one of the id is null
    Given [I.C.9] a client registered with a vehicle and a list of several finished workorder
    And [I.C.9] a null workorder id
    When [I.C.9] I try to create an invoice
    Then [I.C.9] argument is rejected with an explaining message