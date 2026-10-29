"""Simulates the registration rush: every student logs in and enrolls in popular classes at once.

Each Locust user is one distinct seeded student (see scripts/setup/seed_db.py). It logs in, sends one
batch enroll for a few classes drawn from a small hot pool, checks its classes, then stops.
The hot pool is small on purpose, so many students fight over the same seats.

Browser covers only the public class endpoints, so it measures reads without the login cost.

Usage (2000 users spawned within 10s; append Student or Browser to pick one scenario):
    taskset -c 2 locust -f scripts/loadtest/locustfile.py --headless -u 2000 -r 200 -t 60s \
        --host http://localhost --csv scripts/loadtest/results/run Student

Env vars:
    STUDENT_BASE         first student code (default: 20260001)
    STUDENT_PASSWORD     password shared by all seeded students (default: student123)
    HOT_CLASSES          size of the contested class pool (default: 10)
    CLASSES_PER_STUDENT  classes in each enroll request (default: 3)
"""

import itertools
import os
import random
import sys
from collections import Counter

import requests
from locust import FastHttpUser, between, events, task
from locust.exception import StopUser

STUDENT_BASE = int(os.getenv("STUDENT_BASE", "20260001"))
STUDENT_PASSWORD = os.getenv("STUDENT_PASSWORD", "student123")
HOT_CLASSES = int(os.getenv("HOT_CLASSES", "10"))
CLASSES_PER_STUDENT = int(os.getenv("CLASSES_PER_STUDENT", "3"))

API = "/api/v1"

# Single process run, so a plain counter hands each user a unique student code
student_codes = itertools.count(STUDENT_BASE)
all_class_ids = []
hot_class_ids = []
outcomes = Counter()


@events.init.add_listener
def load_classes(environment, **kwargs):
    """Fetches the class ids once before users spawn; the first HOT_CLASSES form the contested pool.

    Doing it per user would add thousands of list calls that are not part of the rush.
    Runs on init and exits the process on failure: Locust only logs listener errors, and a
    quit from test_start leaves a timed run idling with empty stats.
    """
    resp = requests.get(f"{environment.host}{API}/course-classes", params={"page": 0, "size": 100})
    if resp.status_code != 200 or not resp.json():
        sys.exit(f"Cannot load classes ({resp.status_code}); is the app up, the db seeded and the endpoint public?")
    all_class_ids[:] = [c["classId"] for c in resp.json()]
    hot_class_ids[:] = all_class_ids[:HOT_CLASSES]


@events.test_stop.add_listener
def print_outcomes(environment, **kwargs):
    """Prints how the enroll results split by status/reason.

    Locust only sees HTTP status; a 200 can still hold REJECTED or RETRY results per class.
    """
    if not outcomes:
        return
    print("\nEnroll outcomes (per class):")
    for key, count in outcomes.most_common():
        print(f"  {key:<35} {count}")


class Student(FastHttpUser):
    def on_start(self):
        self.username = str(next(student_codes))
        with self.client.post(
            f"{API}/auth/login",
            json={"username": self.username, "password": STUDENT_PASSWORD},
            catch_response=True,
        ) as resp:
            if resp.status_code != 200:
                resp.failure(f"login failed: {resp.status_code}")
                raise StopUser()
            self.auth = {"Authorization": f"Bearer {resp.json()['accessToken']}"}

    @task
    def register(self):
        class_ids = random.sample(hot_class_ids, min(CLASSES_PER_STUDENT, len(hot_class_ids)))
        with self.client.post(
            f"{API}/enrollments",
            json={"classIds": class_ids},
            headers=self.auth,
            catch_response=True,
        ) as resp:
            if resp.status_code != 200:
                resp.failure(f"enroll failed: {resp.status_code}")
            else:
                for item in resp.json()["results"]:
                    outcomes[f"{item['status']} {item['reason'] or ''}".strip()] += 1

        self.client.get(f"{API}/enrollments/my-classes", headers=self.auth)

        # One registration per student; the rush is the spawn, not repeated loops
        raise StopUser()


class Browser(FastHttpUser):
    """A student refreshing the class list and opening classes while waiting for seats.

    Uses only public endpoints, so it runs without login.
    """

    wait_time = between(0.5, 2)

    @task(3)
    def list_classes(self):
        self.client.get(f"{API}/course-classes?page={random.randint(0, 4)}&size=20", name=f"{API}/course-classes")

    @task(1)
    def class_detail(self):
        class_id = random.choice(all_class_ids)
        self.client.get(f"{API}/course-classes/{class_id}", name=f"{API}/course-classes/[classId]")
