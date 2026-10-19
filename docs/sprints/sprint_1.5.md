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

To be discussed.
