# Toucan Payments - Transaction Service

## 1. Problem Understanding

This project implements a small transaction-processing REST service using Java,
Spring Boot, Spring Data JPA, and an H2 in-memory database.

The service manages customer transactions and supports the four operations
specified in the challenge:

- Create a transaction
- Retrieve a transaction by Transaction ID
- Update a transaction's status
- Retrieve all transactions for a Customer ID

The implementation focuses on keeping the solution simple, readable, and easy
to maintain.

---

## 2. Assumptions

- Every newly created transaction starts with `PENDING` status.
- Transaction IDs are unique.
- A transaction that does not exist returns `404 Not Found`.
- The H2 in-memory database provided by the starter project is used.
- No individual candidate variant was provided in the invitation email.
- Therefore, no additional variant-specific amount, currency, transaction-type,
  or other restrictions were invented.
- Authentication and authorization are outside the scope of this challenge.

---

## 3. Validation Rules

The following validation rules are applied when creating a transaction:

| Field | Validation |
|---|---|
| Transaction ID | Required and must not be blank |
| Customer ID | Required and must not be blank |
| Amount | Required and must be greater than zero |
| Currency | Required |
| Transaction Type | Required |

A duplicate Transaction ID is rejected.

For status updates, the status field is required.

Invalid request data results in a `400 Bad Request` response.

---

## 4. Status Transition Rules

New transactions always start in `PENDING`.

The following status transitions are allowed:

```text
PENDING
   ├──> PROCESSING
   └──> CANCELLED

PROCESSING
   ├──> COMPLETED
   └──> FAILED
```
COMPLETED, FAILED, and CANCELLED are treated as terminal states and
cannot be changed to another status.

Reasoning

The rules prevent a completed, failed, or cancelled transaction from being
modified later. This keeps the transaction lifecycle predictable and avoids
moving a transaction backwards after it has reached a final state.

## 5. API Endpoints
Create Transaction
`POST /api/transactions`
```text
Example request:

{
  "transactionId": "TXN-001",
  "customerId": "CUST-001",
  "amount": 500.00,
  "currency": "INR",
  "transactionType": "PAYMENT"
}
```
A successful request returns 201 Created.

Get Transaction
`GET /api/transactions/{transactionId}`

Example:

`GET /api/transactions/TXN-001`

If the transaction does not exist, the service returns 404 Not Found.

Update Transaction Status
`PATCH /api/transactions/{transactionId}/status`

Example request:
```text
{
  "status": "PROCESSING"
}
```

The requested status transition must follow the defined transaction lifecycle.

Get Customer Transactions

`GET /api/transactions/customer/{customerId}`


Example:

`GET /api/transactions/customer/CUST-001`

Returns all transactions belonging to the specified customer.

## 6. Project Structure

The application uses a simple controller-service-repository structure:
```text
Controller
    ↓
Service
    ↓
Repository
    ↓
H2 Database
```
- Controller - Handles HTTP requests and responses.
- Service - Contains transaction business logic and status transition rules.
- Repository - Provides database access using Spring Data JPA.
- Entity - Represents a transaction stored in the database.
- DTOs - Represent and validate incoming API requests.
- Enums - Define supported currencies, transaction types, and statuses.
- Global Exception Handler - Converts application and validation errors
into HTTP responses.
## 7. Testing

The project contains six automated tests using JUnit and Spring MockMvc.

The tests cover:

- A transaction can be created successfully.
- Invalid transaction data is rejected.
- Duplicate Transaction IDs are rejected.
- A non-existent transaction returns 404 Not Found.
- Transactions can be retrieved by Customer ID.
- A transaction status can be successfully updated.

The status transition rules were also manually verified through Postman.

Run the complete test suite with:

`.\mvnw.cmd clean test`

Latest test run:
```text
Tests run: 6
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```
## 8. Error Handling

The application handles the following cases:

- Validation failures → 400 Bad Request
- Duplicate Transaction ID → 400 Bad Request
- Invalid status transitions → 400 Bad Request
- Missing transaction → 404 Not Found

Errors are returned as a simple JSON response, for example:
```text
{
  "message": "Transaction not found"
}
```
## 9. Known Limitations
- The database is an H2 in-memory database, so data is not persisted after
application shutdown.
- There is no authentication or authorization.
- The API does not implement pagination or sorting for customer transactions.
- The automated tests do not cover every possible status-transition branch.
- No candidate-specific variant was provided, so variant-specific validation
rules could not be implemented.
## 10. What I Would Improve With More Time

With more time, I would consider:

- Adding more automated tests for individual status transitions.
- Adding more detailed validation and error responses if business requirements
were provided.
- Using a persistent database for production use.
- Adding API documentation such as OpenAPI/Swagger.
- Adding authentication and authorization if required.
- Adding integration and production-level configuration and observability.