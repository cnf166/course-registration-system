# Sprint 1 — Project Skeleton

**Duration:** Mon 2026-10-05 → Thu 2026-10-15

**Goal:** every layer of the system is laid out and runnable, so that feature work in the next sprint only fills in logic. The three tasks run in parallel; nobody waits for another task to finish.

**Stretch goal (end of week 1):** the API skeleton is hosted somewhere reachable, with Swagger UI open for the team. Vinh handles the hosting.

| Dev | Task |
|---|---|
| Việt | API controller layout + DTOs + Swagger docs |
| Việt Anh | Database, entity mapping, auth service |
| Vinh | System bootstrapping (business layer, DI, service layout) |

References: [API specs](../specs/api-specs.md), [architecture](../specs/architecture.md), [DB schema](../specs/db_schema.md).

---

## Task 1 — API controller layout (Việt)

Lay out the API layer without waiting for the services. Design the DTO classes as you see fit; we adjust them when real logic is wired in and we see how they feel in use.

**Scope**

- One controller class per resource, declaring all 20 endpoints in the API specs. Bodies are stubs (return `501 Not Implemented`).
- Request and response DTOs, with validation annotations on requests.
- Swagger annotations on every endpoint: summary, response codes, bearer auth on protected endpoints, grouped by tag.
- `ErrorResponse` and a `GlobalExceptionHandler` shell.

**Definition of done**

- Code is merged into `develop`.
- Swagger UI shows the full contract: every endpoint, with request and response shapes, following the [OpenAPI conventions](https://swagger.io/open-api/).
- No controller returns a JPA entity or contains business logic.
- Spec fields that have no table yet (prerequisites, instructor, transcript, class status) are laid out and marked as not backed.

## Task 2 — Database, entities, auth service (Việt Anh)

**Scope**

- JPA entities matching the DB schema, and a Spring Data repository per entity.
- Auth end to end: login, refresh token, change password, JWT filter, password hashing, role rules as in the permission column of the API specs.
- Login accepts either email or student code. Add a unique constraint on `student_id` (entity and DB schema doc).
- Owns `security/`, `business/auth` and `business/user` (model, repository port and its adapter). This slice is the reference for how a module is layered.
- `DataSeeder` with one admin and one student, enough to log in.
- **First deliverable:** a minimal `SecurityConfig` that permits Swagger UI and the public routes. Until this lands, Spring Security blocks every endpoint and Task 1 cannot be viewed.

**Definition of done**

- Design doc and code are merged into `develop`.
- The app boots against an empty PostgreSQL database.
- Logging in through Swagger with an email or a student code returns a token; the token opens a protected stub endpoint, and a wrong role gets `403`.
- Design doc covers: token lifetime, refresh strategy, where roles are checked.

## Task 3 — System bootstrapping (Vinh)

**Scope**

- `business/` packages for `course`, `courseclass`, `enrollment`, `semester`: business model, service class (method signatures, empty bodies) and repository port for each.
- Business exception base class and types.
- `BeanConfig` wiring the business services; `business/` imports nothing from Spring or JPA.

**Definition of done**

- Design doc and code are merged into `develop`.
- The app boots with all services wired.
- One module (not auth) has a thin working path from controller to database, as a template for the others.
- Design doc covers: layering rules, how a request flows from controller to service to port, how to add a new module.

---

## Working agreements

- Design docs go in `docs/`, written alongside the implementation, not after.
- Diagrams are written in Mermaid. Use [vpascode.com](https://vpascode.com) to view them.
- Seam between Task 2 and Task 3: Task 3 defines the repository ports; adapters for `course`, `courseclass`, `enrollment` and `semester` are written next sprint, once the ports exist.
