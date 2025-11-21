import pytest
import requests
import re

def test_generate_returns_code(api_base):
    res = requests.post(f"{api_base}/codes/generate")
    assert res.status_code == 200
    data = res.json()
    assert "id" in data and "code" in data
    assert len(data["code"]) == 6

def test_test_endpoint_reports_good_or_bad(api_base):
    # create a known bad code by generating many times until we find one with a symbol
    found = None
    for _ in range(20):
        r = requests.post(f"{api_base}/codes/generate").json()
        if re.search(r'[^a-z1-9]', r["code"]):
            found = r
            break
    if not found:
        # fallback: create a code with symbol by generating more or skip
        pytest.skip("Could not generate an initial bad code; skipping BAD-code test")
    r2 = requests.post(f"{api_base}/codes/{found['id']}/test")
    assert r2.status_code == 200
    assert "BAD" in r2.json().get("message", "")

def test_cleanup_replaces_symbols(api_base):
    # generate until a bad code is produced then call cleanup
    bad = None
    for _ in range(30):
        r = requests.post(f"{api_base}/codes/generate").json()
        if re.search(r'[^a-z1-9]', r["code"]):
            bad = r
            break
    if not bad:
        pytest.skip("No bad code generated; skipping cleanup test")
    r = requests.post(f"{api_base}/codes/{bad['id']}/cleanup")
    assert r.status_code == 200
    # fetch list and confirm code has no symbols
    all_codes = requests.get(f"{api_base}/codes").json()
    rec = next((x for x in all_codes if x["id"] == bad["id"]), None)
    assert rec is not None
    assert re.fullmatch(r'[a-z1-9]{6}', rec["code"])