# Deployment

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
- Docker Compose (`docker compose`, not `docker-compose`)

## Configuration

| Variable          | Default (in container) | Description                     |
|-------------------|------------------------|---------------------------------|
| `DAO_SQLITE_PATH` | `/app/data/app.db`     | SQLite database file used by `DaoSqlite` |

The database lives in `./data` on the host, bind-mounted to `/app/data` in the `app` container, so it survives rebuilds and container removal. The app creates `app.db` inside it on first start.

> Create `./data` yourself before the first `docker compose up`. If it is missing, Docker creates it owned by `root`, and you can no longer replace or seed the db without `sudo`.

## Deploy

1. Get the code on the server:

   ```bash
   git clone <repo-url> course-registration-system
   cd course-registration-system
   git checkout master
   ```

2. Create the data folder, then build and start:

   ```bash
   mkdir -p data
   docker compose up -d --build
   ```

   To start from an existing db or fake data, put it in `data/` before `docker compose up`: copy it to `data/app.db`, or see [Seed fake data](#seed-fake-data).

3. Verify:

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

### Seed fake data

`scripts/seed_db.py` overwrites `data/app.db` with fake data: an `admin` / `admin123` account, students `20260001`… / `student123`, classes with schedules, and an open enrollment period. Stop the app first; otherwise it keeps writing to the deleted db and never sees the new one.

```bash
pip install -r scripts/requirements.txt
docker compose stop app
python3 scripts/seed_db.py        # --students, --classes, --seed; see --help
docker compose start app
```

If it fails with `Permission denied`, `./data` was created by Docker; run `sudo chown -R $USER data` and retry.

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

- The build step do cache the dependencies. If need a clean build, run `docker compose build --no-cache`, this should be helpful.
- Tests are skipped in the image build (`-DskipTests`); run `./mvnw test` locally or in CI before deploying.
- nginx serves plain HTTP only. To add TLS, mount certificates into the `nginx` service, add a `listen 443 ssl` server block in `nginx/default.conf`, and publish `443:443`.
