# Sprint 2 — Services, Endpoints and Load Test Plan

**Duration:** TBD

**Goal:** make the MVP work end to end and correct under concurrent requests: every endpoint in the [API specs](../specs/api-specs.md) runs real business logic on the SQLite persistence layer, and registration never overbooks a class or creates a duplicate enrollment. In parallel, the team agrees on how the system will be load tested, so the environment can be built in the next sprint.

References: [Sprint 1.5](sprint_1.5.md), [API specs](../specs/api-specs.md), [architecture](../specs/architecture.md), [DB schema](../specs/db_schema.md).

---

## Starting point

- Persistence is done: DaoSqlite entities and repository adapters for users, classes, class schedules, enrollments and the enrollment period.
- The business layer has models, repository ports and service stubs (`ClassCourseService`, `EnrollmentService`, `EnrollmentPeriodService`). The services are not Spring beans yet.
- Controllers and DTOs exist but return stubs. Controllers that are out of scope (`Course`, `Semester`, `Schedule`, `Transcript`) are still in the code.
- Spring Security runs with its default config, so every endpoint returns `401`.

## Deliverables

- **Services** — business logic for auth, classes, enrollment period and enrollments, registered as Spring beans.
- **API endpoints** — controllers wired to the services, security configured with JWT and roles.
- **Load test plan** — a written decision on tool, scenarios, environment and metrics. No implementation in this sprint.

## Tasks

| Task | Lead | With |
|---|---|---|
| 1. Services | TBD | — |
| 2. API endpoints and security | TBD | — |
| 3. Load test plan (discussion) | TBD | Whole team (meeting) |

### Task 1 — Services

**Scope**

- `AuthService`: login with `username` and password, password hashing, issue access and refresh tokens.
- `UserService`: current user profile, change password.
- `ClassCourseService`: create, get, list with `enrolledCount` and `availableSeats`.
- `EnrollmentPeriodService`: get current period, update it (not allowed while the period is open).
- `EnrollmentService`: enroll in many classes in one request, each class checked on its own (period open, class exists and has seats, no schedule conflict, credit limit, not already enrolled); cancel; list my classes with `totalCredits`.
- Register the services as Spring beans.
- Seed a first `SYSTEM_ADMIN` account (method to be agreed).

> **Notes**
>
> - Proper concurrent handling of registration is required in this phase: no class overbooked and no duplicate enrollment under concurrent requests.
> - Credit limit: 26 credits per student.
> - Open question — refresh tokens: storage and rotation policy are not decided.

**Definition of done**

- Every business rule in API spec §5.1 returns the matching `reason` code.
- Unit tests cover each rejection reason.
- Merged into `develop`.

### Task 2 — API endpoints and security

**Scope**

- Wire every controller in the API spec to its service; remove the out-of-scope controllers.
- Security config: JWT bearer filter, `Public` / `Authenticated` / role permissions as in the spec, student identity taken from the JWT only.
- `GlobalExceptionHandler` maps business errors to the shared error response and HTTP status table (API spec §6).
- Update the API spec login field from `identifier` to `username`.

**Definition of done**

- Every endpoint in the spec works from Swagger UI with the right permission.
- No endpoint returns `501` or an entity.
- Merged into `develop`.

### Task 3 — Load test plan (discussion only)

The team decides how to load test the system. The result is a short document; the Docker Compose environment and test scripts are built in the next sprint.

**Questions to answer**

- **Tool:** k6, JMeter, Gatling or Locust.
- **Environment:** which services go into Docker Compose (app, load generator, metrics/dashboards), and the resource limits for the app container so results are repeatable.
- **Scenarios:** at least the registration-open surge (many students enrolling at the same moment) and the last-seat race (many students competing for one class).
- **Test data:** number of students, classes and capacities, and how they are seeded.
- **Metrics and pass criteria:** throughput, latency percentiles, error and `503` rates, and correctness checks after the run (no class over capacity, no duplicate enrollment, no lost enrollment).

**Definition of done**

- The team has confirmed the plan in the meeting.
- The plan is written in `docs/` and merged into `develop`.
