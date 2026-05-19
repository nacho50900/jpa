# Project Architecture and Testing Guide

## Overview
This project is organized into several modules, each with a specific responsibility in the overall application. The architecture is designed to separate concerns, facilitate maintainability, and support robust testing at different levels.

## Module Purposes
- **cws_service_interfaces**: Defines service interfaces for business operations.
- **cws_services_jpa**: Implements business logic and persistence using JPA.
- **cws_client_cashier**: Client application for cashier-related operations.
- **cws_client_foreman**: Client application for foreman-related operations.
- **cws_client_manager**: Client application for manager-related operations.
- **cws_client_mechanic**: Client application for mechanic-related operations.
- **cws_aceptance_tests**: Contains acceptance (Cucumber) tests for validating business requirements and end-to-end scenarios.
- **cws_util**: Utility classes and helpers shared across modules.

## Testing Strategy
### 1. Domain Unit Tests
- **Base Unit Tests**: Mandatory for all students. These tests cover the core domain logic and must be implemented and passed.
- **Extended Unit Tests**: Also mandatory. These tests cover additional or edge-case domain logic and must be implemented and passed.

### 2. Acceptance (Cucumber) Tests
- Acceptance tests are located in the `cws_aceptance_tests` module.
- **First (January) Call**: You must pass only the acceptance tests that correspond to your UO code (student code). Each student is responsible for the features/scenarios assigned to their UO code.
- **Other Calls**: You must pass all acceptance tests.

## How to Run the Tests
### Running Unit Tests (Base and Extended)
1. Open the project in Eclipse.
2. Navigate to the relevant module (e.g., `cws_services_jpa`).
3. Right-click the `test` or `test_extended` folder and select `Run As > JUnit Test`.
   - Repeat for both base and extended tests.

### Running Acceptance (Cucumber) Tests
1. Open the `cws_aceptance_tests` module in Eclipse.
2. Right-click the `test\uo.ri.cws.application.service.RunCucumberTests_X.java` file and select `Run As > JUnit Test`.
3. Ensure you only run the scenarios required for your UO code (X) in the January call. In other calls, all scenarios must pass.

## Notes
- Your UO code determines which acceptance tests are mandatory in the January call. Make sure you know your assigned features/scenarios.
- All base and extended unit tests are always mandatory.
- Acceptance tests are organized by feature and scenario for clarity and maintainability.

For more details on the acceptance test structure and support code, see the `cws_aceptance_tests/README.md`.
