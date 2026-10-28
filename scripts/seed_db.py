#!/usr/bin/env python3
"""Creates a fresh SQLite db filled with fake users, classes and an open enrollment period.

Gives dev and load testing a ready db without going through the API. The schema mirrors
what DaoSqlite creates from the data/entity classes, so keep it in sync when an entity changes.
An existing db at the target path is overwritten.

Usage: python3 scripts/seed_db.py [--db PATH] [--students N] [--classes N] [--seed N]
"""

import argparse
import random
import sqlite3
import sys
import uuid
from datetime import datetime, timedelta, timezone
from pathlib import Path

try:
    import bcrypt
except ImportError:
    sys.exit("Missing dependency: pip install -r scripts/requirements.txt")

ROOT = Path(__file__).resolve().parent.parent

ADMIN_USERNAME = "admin"
ADMIN_PASSWORD = "admin123"
STUDENT_PASSWORD = "student123"

# as in data/define/UserRole.java
ROLE_SYSTEM_ADMIN = 1
ROLE_STUDENT = 255

# as in data/define/Day.java (MONDAY..SATURDAY) and data/define/DayShift.java (SHIFT_1..SHIFT_4)
DAYS = range(2, 8)
SHIFTS = range(1, 5)

# Same DDL DaoSqlite generates; version 1 rows in _dao_versions stop it from migrating the tables
SCHEMA = """
CREATE TABLE _dao_versions (table_name TEXT PRIMARY KEY NOT NULL, version    INTEGER NOT NULL DEFAULT 1);
CREATE TABLE users (id TEXT PRIMARY KEY, username TEXT NOT NULL UNIQUE, name TEXT NOT NULL, password_hash TEXT NOT NULL, role INTEGER NOT NULL, created_at TEXT NOT NULL);
CREATE TABLE classes (id TEXT PRIMARY KEY, name TEXT NOT NULL, credits INTEGER NOT NULL, capacity INTEGER NOT NULL, created_at TEXT NOT NULL);
CREATE TABLE class_schedules (id TEXT PRIMARY KEY, class_id TEXT NOT NULL, day_of_week INTEGER NOT NULL, shift INTEGER NOT NULL);
CREATE TABLE enrollment_period (id TEXT PRIMARY KEY, start_at TEXT NOT NULL, end_at TEXT NOT NULL, updated_at TEXT NOT NULL);
CREATE TABLE enrollments (id TEXT PRIMARY KEY, student_id TEXT NOT NULL, class_id TEXT NOT NULL, enrolled_at TEXT NOT NULL);
INSERT INTO _dao_versions (table_name, version) VALUES
    ('users', 1), ('classes', 1), ('class_schedules', 1), ('enrollment_period', 1), ('enrollments', 1);
"""

LAST_NAMES = ["Nguyen", "Tran", "Le", "Pham", "Hoang", "Huynh", "Phan", "Vu", "Vo", "Dang", "Bui", "Do", "Ho", "Ngo", "Duong", "Ly"]
MIDDLE_NAMES = ["Van", "Thi", "Minh", "Duc", "Thanh", "Ngoc", "Quoc", "Gia", "Hoang", "Bao", "Anh", "Huu"]
FIRST_NAMES = ["An", "Binh", "Chau", "Dung", "Giang", "Ha", "Hieu", "Hung", "Khanh", "Lan", "Linh", "Long", "Mai", "Nam", "Phuc", "Quan", "Son", "Tam", "Trang", "Tuan", "Viet", "Vinh", "Yen"]

COURSES = [
    "Introduction to Programming", "Data Structures and Algorithms", "Discrete Mathematics",
    "Calculus", "Linear Algebra", "Probability and Statistics", "Computer Architecture",
    "Operating Systems", "Computer Networks", "Database Systems", "Software Engineering",
    "Software Architecture", "Object-Oriented Programming", "Web Development",
    "Mobile Application Development", "Artificial Intelligence", "Machine Learning",
    "Computer Graphics", "Information Security", "Distributed Systems", "Cloud Computing",
    "Compiler Construction", "Human-Computer Interaction", "Embedded Systems", "Data Mining",
]


def to_db_time(dt):
    """Formats a datetime like TimeUtils.toDbString so the app can parse it back."""
    return dt.astimezone(timezone.utc).strftime("%Y-%m-%dT%H:%M:%S.") + f"{dt.microsecond // 1000:03d}Z"


def new_id():
    """Generates an id like IdUtils.generateUuid (uuid4 without dashes)."""
    return uuid.uuid4().hex


def spring_bcrypt(password):
    """Hashes a password in the {bcrypt} format Spring's DelegatingPasswordEncoder reads."""
    return "{bcrypt}" + bcrypt.hashpw(password.encode(), bcrypt.gensalt(rounds=10)).decode()


def reset_db(path):
    """Deletes the db and its WAL/SHM files so the new db starts empty."""
    path.parent.mkdir(parents=True, exist_ok=True)
    for suffix in ("", "-wal", "-shm"):
        Path(str(path) + suffix).unlink(missing_ok=True)


def fake_name(rng):
    return f"{rng.choice(LAST_NAMES)} {rng.choice(MIDDLE_NAMES)} {rng.choice(FIRST_NAMES)}"


def seed_users(conn, rng, students, now):
    # Students share one hash; bcrypt per student would take minutes at MVP size
    student_hash = spring_bcrypt(STUDENT_PASSWORD)
    rows = [(new_id(), ADMIN_USERNAME, "System Admin", spring_bcrypt(ADMIN_PASSWORD), ROLE_SYSTEM_ADMIN, to_db_time(now))]
    rows += [
        (new_id(), f"{20260000 + i}", fake_name(rng), student_hash, ROLE_STUDENT, to_db_time(now))
        for i in range(1, students + 1)
    ]
    conn.executemany("INSERT INTO users VALUES (?, ?, ?, ?, ?, ?)", rows)


def seed_classes(conn, rng, classes, now):
    for i in range(classes):
        class_id = new_id()
        # Cycle through the courses so each one gets several numbered groups
        name = f"{COURSES[i % len(COURSES)]} ({i // len(COURSES) + 1:02d})"
        conn.execute(
            "INSERT INTO classes VALUES (?, ?, ?, ?, ?)",
            (class_id, name, rng.choice([2, 3, 3, 4]), rng.choice([30, 40, 50, 60, 80]), to_db_time(now)),
        )

        # 1-2 distinct weekly slots; id format as in ClassScheduleEntity.idOf
        all_slots = [(d, s) for d in DAYS for s in SHIFTS]
        for day, shift in rng.sample(all_slots, rng.choice([1, 2])):
            conn.execute(
                "INSERT INTO class_schedules VALUES (?, ?, ?, ?)",
                (f"{class_id}:{day}:{shift}", class_id, day, shift),
            )


def seed_period(conn, now, days_open):
    # Single shared period (EnrollmentPeriodEntity.CURRENT_ID), open from now
    conn.execute(
        "INSERT INTO enrollment_period VALUES (?, ?, ?, ?)",
        ("current", to_db_time(now), to_db_time(now + timedelta(days=days_open)), to_db_time(now)),
    )


def main():
    parser = argparse.ArgumentParser(description=__doc__.splitlines()[0])
    parser.add_argument("--db", type=Path, default=ROOT / "data" / "app.db", help="db file to create (default: ./data/app.db)")
    parser.add_argument("--students", type=int, default=2000, help="number of students (default: 2000)")
    parser.add_argument("--classes", type=int, default=100, help="number of classes (default: 100)")
    parser.add_argument("--period-days", type=int, default=14, help="days the enrollment period stays open (default: 14)")
    parser.add_argument("--seed", type=int, default=None, help="random seed for reproducible data")
    args = parser.parse_args()

    rng = random.Random(args.seed)
    now = datetime.now(timezone.utc)

    # A data dir created by Docker is root-owned, so the user can't replace the db in it
    try:
        reset_db(args.db)
    except PermissionError:
        sys.exit(f"Permission denied on {args.db.parent}; it was likely created by Docker. Run: sudo chown -R $USER {args.db.parent}")
    with sqlite3.connect(args.db) as conn:
        conn.executescript(SCHEMA)
        seed_users(conn, rng, args.students, now)
        seed_classes(conn, rng, args.classes, now)
        seed_period(conn, now, args.period_days)
    conn.close()

    print(f"Created {args.db}")
    print(f"  admin:    {ADMIN_USERNAME} / {ADMIN_PASSWORD}")
    print(f"  students: {args.students} ({20260001}..{20260000 + args.students}) / {STUDENT_PASSWORD}")
    print(f"  classes:  {args.classes}, enrollment period open for {args.period_days} days")


if __name__ == "__main__":
    main()
