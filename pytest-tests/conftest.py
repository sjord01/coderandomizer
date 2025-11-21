import pytest
import os
import requests
import time

API_BASE = os.environ.get("API_BASE", "http://localhost:8080/api")

@pytest.fixture(scope="session")
def api_base():
    # Wait for backend to become available
    for _ in range(30):
        try:
            r = requests.get(f"{API_BASE}/codes")
            if r.status_code == 200:
                break
        except Exception:
            pass
        time.sleep(1)
    return API_BASE