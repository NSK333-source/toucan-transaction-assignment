# AI Usage Disclosure

## Tools Used

I used ChatGPT and GitHub Copilot/Codex as AI-assisted development tools while
working on this assignment.

## How AI Was Used

AI tools were used primarily to:

- Understand the assignment requirements and break them into smaller tasks.
- Discuss possible approaches for implementing the transaction service.
- Review and improve Java and Spring Boot code.
- Help identify validation and error-handling issues.
- Suggest and review automated tests.
- Review the project against the challenge checklist.
- Help prepare the README documentation.

## Significant AI Suggestions

AI suggested using a simple controller-service-repository structure and helped
with implementing request validation, duplicate Transaction ID checking,
transaction status transitions, and global exception handling.

AI also suggested returning `404 Not Found` when a requested transaction does
not exist, rather than treating it as a general bad request.

## What I Changed, Corrected, or Rejected

I reviewed the suggestions and made the final implementation decisions myself.
I removed unnecessary repository filtering logic when it was no longer required
by the four operations in the challenge.

I also kept the implementation intentionally simple rather than adding
additional functionality that was not required.

The status transition rules were chosen as:

PENDING -> PROCESSING or CANCELLED

PROCESSING -> COMPLETED or FAILED

COMPLETED, FAILED, and CANCELLED are terminal states.

These rules were reviewed and manually tested using Postman.

## What AI Got Wrong

During development, the initial error handling treated a missing transaction as
a `400 Bad Request`. After reviewing the requirement and testing the API, this
was changed to return `404 Not Found`.

AI also suggested additional tests and functionality beyond the minimum
requirements. I reviewed these suggestions and chose not to add unnecessary
complexity where it did not provide enough value for this assignment.

## How I Verified the Final Result

I manually tested the REST endpoints using Postman, including transaction
creation, retrieval, customer transaction lookup, status updates, duplicate
Transaction IDs, invalid input, and invalid status transitions.

I also ran the complete Maven test suite:

    .\mvnw.cmd clean test

The final test run completed successfully:

    Tests run: 6
    Failures: 0
    Errors: 0
    Skipped: 0

    BUILD SUCCESS