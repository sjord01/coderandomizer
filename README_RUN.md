# Quick start — Run app and tests locally

Copy/paste these commands to build, run, and test the application locally.

1) Run entire stack with Docker Compose (recommended)

```bash
# from repository root
docker compose up --build
# open the UI at http://localhost:5173 and the API at http://localhost:8080/api
```

Stop the stack:

```bash
docker compose down
```

2) Run tests (while Docker Compose is running or backend/front-end started locally)

- API tests (pytest):

```bash
# optional: create and activate a virtualenv
python -m venv .venv
source .venv/bin/activate

pip install -r pytest-tests/requirements.txt
# run the API tests (conftest waits for backend)
cd pytest-tests
pytest -v
```

- Playwright UI tests (run locally):

```bash
# in a separate terminal ensure backend + frontend are running (docker compose up --build)
cd frontend
npm ci
# install Playwright browsers (required once)
npx playwright install --with-deps
# run Playwright tests (unix/mac)
API_BASE=http://localhost:8080/api npx playwright test
# or use the npm script
npm run test:e2e:ci

# view HTML report after failures or runs
npx playwright show-report
```

3) Run backend and frontend locally (no Docker)

Backend:
```bash
cd backend
mvn -DskipTests spring-boot:run
# or build and run the jar
mvn -B -DskipTests package
java -jar target/coderandomizer-0.0.1-SNAPSHOT.jar
```

Frontend:
```bash
cd frontend
npm ci
npm run dev
# or build and preview
npm run build
npx vite preview --port 5173 --strictPort
```

Notes:
- The Playwright tests use the API_BASE environment variable to point the tests at the backend API. The example commands set API_BASE=http://localhost:8080/api which is the default when running the backend on the host or with Docker Compose.
- If you run the frontend inside Docker Compose, ensure frontend/vite.config.js proxies /api to http://backend:8080.
- If you run into TypeScript lib errors for Promise, the repository already includes frontend/tsconfig.json to target a modern ES lib.
