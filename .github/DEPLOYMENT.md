# Deployment

The service is deployed with Docker Compose as two containers:

```text
client ──:80──▶ nginx ──:8080──▶ app (Spring Boot, JRE 21) ──▶ /app/data/app.db
                                                                   │
                                                          bind mount ./data
```

| Service | Image                               | Role                                                   |
|---------|-------------------------------------|--------------------------------------------------------|
| `app`   | `course-registration-system:latest` | Built from `Dockerfile`; port 8080 is internal only    |
| `nginx` | `nginx:1.27-alpine`                 | Reverse proxy, the only service published on host `:80` |

## Files

| File                  | Purpose                                                                 |
|-----------------------|-------------------------------------------------------------------------|
| `Dockerfile`          | Multi-stage build: `eclipse-temurin:21-jdk` builds the jar, `eclipse-temurin:21-jre` runs it |
| `.dockerignore`       | Keeps `target/`, `data/`, `.git/`, docs, etc. out of the build context  |
| `docker-compose.yml`  | Defines the `app` and `nginx` services and the data volume              |
| `nginx/default.conf`  | nginx site config proxying `/` to `app:8080`                            |

## Prerequisites

- Docker Engine 23+ (BuildKit is required for the `--mount=type=cache` build step)
- Docker Compose v2 (`docker compose`, not `docker-compose`)
- Host port 80 free

No local JDK or Maven is needed; the build runs inside the JDK image.

## Configuration

| Variable          | Default (in container) | Description                     |
|-------------------|------------------------|---------------------------------|
| `DAO_SQLITE_PATH` | `/app/data/app.db`     | SQLite database file used by `DaoSqlite` |

The database lives in `./data` on the host, bind-mounted to `/app/data` in the `app` container, so it survives rebuilds and container removal. If `./data` does not exist, Docker creates it on first start and the app creates `app.db` inside it.

> Files written by the container are owned by `root` on the host. Use `sudo` (or a container) to delete or replace them.

## Deploy

1. Get the code on the server:

   ```bash
   git clone <repo-url> course-registration-system
   cd course-registration-system
   git checkout master
   ```

2. (Optional) Seed an existing database:

   ```bash
   mkdir -p data
   cp /path/to/app.db data/app.db
   ```

3. Build and start:

   ```bash
   docker compose up -d --build
   ```

4. Verify:

   ```bash
   docker compose ps
   docker compose logs -f app        # wait for "Started SoftwareArchitectApplication"
   curl -i http://localhost/swagger-ui/index.html
   ```

   A `401` response means the request reached the app through nginx and was rejected by Spring Security, so the stack is up. A `502` means nginx is running but the app is not (check `docker compose logs app`).

## Update to a new version

```bash
git pull
docker compose up -d --build
```

Only `app` is rebuilt and recreated; `./data` is untouched.

## Operations

| Task                 | Command                                                   |
|----------------------|-----------------------------------------------------------|
| Stop                 | `docker compose stop`                                     |
| Stop and remove      | `docker compose down` (data in `./data` is kept)          |
| Restart app only     | `docker compose restart app`                              |
| Tail logs            | `docker compose logs -f app nginx`                        |
| Shell into app       | `docker compose exec app sh`                              |
| Reload nginx config  | `docker compose exec nginx nginx -s reload`               |

### Backup

SQLite runs in WAL mode, so copy `app.db` together with its `-wal`/`-shm` files, or stop the app first to get a single consistent file:

```bash
docker compose stop app
cp data/app.db backups/app-$(date +%F).db
docker compose start app
```

### Restore

```bash
docker compose stop app
cp backups/app-YYYY-MM-DD.db data/app.db
rm -f data/app.db-wal data/app.db-shm
docker compose start app
```

## Notes

- Tests are skipped in the image build (`-DskipTests`); run `./mvnw test` locally or in CI before deploying.
- nginx serves plain HTTP only. To add TLS, mount certificates into the `nginx` service, add a `listen 443 ssl` server block in `nginx/default.conf`, and publish `443:443`.
