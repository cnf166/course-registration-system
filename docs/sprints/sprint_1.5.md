# Sprint 1.5 — Refine After Instructor Review

**Duration:** Mon 2026-10-19 → Thu 2026-10-22, 07:00

**Goal:** narrow the project to one well-defined problem and document it properly before more code is written: the problem statement, the business specification and the user flows. The API spec and the code are then trimmed to match.

References: [Sprint 1](sprint_1.md), [API specs](../specs/api-specs.md), [architecture](../specs/architecture.md), [DB schema](../specs/db_schema.md).

---

## Instructor review

1. The scope is too broad. Pick one core problem and cut or defer everything that does not serve it.
2. Write a problem statement: who has the problem, what goes wrong today, and what the system must guarantee.
3. Write a business specification (actors, rules, constraints) and user flow documents. Sequence diagrams are not required.
4. Some endpoints in the [API specs](../specs/api-specs.md) are marked "Public / Auth", which is ambiguous. If anonymous and signed-in callers get different behavior, describe each case separately; if they get the same behavior, mark it Public.

## Deliverables

- **Problem statement** — the one problem the system solves, and what is out of scope.
- **Business specification** — actors, business rules and constraints for the chosen problem.
- **User flows** — one flow per main use case, written in Mermaid.
- **Revised API spec** — endpoints cut to the new scope, with no merged "Public / Auth" permission.
- **Code aligned to the revised spec** — endpoints, DTOs and entities removed or deferred where they fall out of scope.

## Focus

The system solves one problem: **keeping course registration fast and correct while registration is open**, when every student hits the system at the same moment.

Key architecture characteristics:

- **Scalability** — absorb the traffic surge when a registration period opens.
- **Reliability** — every registration is handled correctly: no class overbooked, no accepted registration lost. When it conflicts with availability at peak load, reliability wins: the system may slow down or ask students to retry, but never gives a wrong answer.

## Tasks

| Task | Lead | With |
|---|---|---|
| 1. Problem statement and business specification | Vinh | Whole team (meeting) |
| 2. User flows | Việt | Reviewed by Vinh |
| 3. Revised API spec and code alignment | Việt Anh | — |

### Task 1 — Problem statement and business specification (Vinh)

The team agrees on the problem and the rules in a meeting; Vinh writes the document.

**Scope**

- Problem statement: who has the problem, what goes wrong at registration time, what the system must guarantee, and what is out of scope.
- Business specification: actors, use cases, business rules and constraints for registration.

**Definition of done**

- The team has agreed on the content in the meeting.
- The document is merged into `develop`.

### Task 2 — User flows (Việt)

**Scope**

- One user flow per use case in the business specification, written in Mermaid.

**Definition of done**

- Every use case in the business specification has a flow.
- Reviewed by Vinh and merged into `develop`.

### Task 3 — Revised API spec and code alignment (Việt Anh)

**Scope**

- Cut the API spec to the endpoints the user flows need.
- Give every endpoint one permission; no merged "Public / Auth".
- Remove or defer the endpoints, DTOs and entities that fall out of scope.

**Definition of done**

- The revised API spec and code are merged into `develop`.
- Every endpoint in the spec is used by at least one user flow.
- Swagger UI matches the revised spec.
