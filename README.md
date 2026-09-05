# IPO Analysis Agent

An explainable Indian IPO research platform that combines financial quality, valuation, issue structure, GMP, subscription demand and cited risks into separate attractiveness and confidence scores.

> Research assistance only—not personalized financial advice. GMP is unofficial and listing gains are never guaranteed. The included companies and figures are clearly marked demo data.

## What is implemented

- Interactive Angular dashboard with search, status filters and responsive IPO cards
- Detailed recommendation drawer with factor-level scores and explanations
- Spring Boot REST API and OpenAPI UI
- Deterministic scoring engine; the LLM is not allowed to invent or calculate financial values
- Separate overall score and data-confidence score
- Financial history and time-stamped GMP/subscription snapshots with provenance
- MySQL schema through Flyway; optional demo data through the `demo` profile
- Live SEBI, NSE and BSE public-page collectors with retry and authoritative provenance
- RHP/DRHP PDF validation, extraction, hashing and SSRF host allowlisting
- Risk, valuation, source, document, analysis-history and background-job models
- Ollama and OpenAI-compatible structured narrative providers with a safe disabled fallback
- Financial and GMP charts, source links, risks and comparison selection
- Unit tests for strong financials and insufficient-data behavior
- Docker deployment for frontend, backend and MySQL

## Run everything

Requirements: Docker Desktop.

```bash
docker compose up --build
```

- Dashboard: http://localhost:4200
- API: http://localhost:8080/api/v1/ipos
- Swagger: http://localhost:8080/swagger-ui.html
- Health: http://localhost:8080/actuator/health

## Development

Backend requires Java 21 and Maven 3.9+:

```bash
mvn spring-boot:run
mvn test
```

Frontend requires Node 22+:

```bash
cd frontend
npm install
npm start
npm run build
```

Docker Compose enables the `demo` profile by default. Set `SPRING_PROFILES_ACTIVE=default` for a clean production database.

### MySQL 8.4 authentication error

MySQL 8.4 no longer loads `mysql_native_password` by default. If an existing database volume or user was created with that legacy plugin, migrate the application user once:

```bash
docker compose exec mysql mysql -uroot -plocal_root_password \
  -e "ALTER USER 'ipo_user'@'%' IDENTIFIED WITH caching_sha2_password BY 'ipo_password'; FLUSH PRIVILEGES;"
docker compose restart backend
```

For a non-Docker MySQL installation, sign in as an administrator and run [`scripts/mysql-auth-migration.sql`](scripts/mysql-auth-migration.sql), changing the username, host and password to match your environment.

To verify the current authentication plugin:

```sql
SELECT user, host, plugin FROM mysql.user WHERE user = 'ipo_user';
```

The expected plugin is `caching_sha2_password`. On a disposable development database, deleting and recreating the Docker volume also fixes it, but permanently deletes that volume's data:

```bash
docker compose down -v
docker compose up --build
```

## Live-source configuration

The application uses these official public pages by default and runs discovery at startup and every six hours:

```text
IPO_SEBI_FEED_URL=https://www.sebi.gov.in/filings/public-issues.html
IPO_NSE_FEED_URL=https://www.nseindia.com/market-data/all-upcoming-issues-ipo
IPO_BSE_FEED_URL=https://www.bseindia.com/markets/publicissues/ipoissues?Type=P&id=1
```

These are public HTML pages, not guaranteed APIs. A source that changes markup or rejects automated requests is reported independently at `GET /api/v1/ipos/discovery-status`; no synthetic data is generated. Override a URL only with an equivalent page whose collection terms you are permitted to use.

For local Ollama:

```bash
ollama pull llama3.2:3b
ollama serve
```

Then set `IPO_AI_PROVIDER=ollama`, `IPO_AI_BASE_URL=http://localhost:11434`, and `IPO_AI_MODEL=llama3.2:3b`. When the backend runs in Docker Compose, its default Ollama URL is `http://host.docker.internal:11434`. Ollama analyzes extracted RHP/DRHP text and stores source-linked risks; numeric formulas, confidence, hard-risk overrides, and the final verdict remain deterministic Java logic. A missing document or unavailable Ollama produces a `PARTIAL` job rather than discarding the deterministic result.

For an IPO discovered as `example-limited`, ingest an official PDF and then queue analysis (use the generated Spring Security username `user` and the password printed at startup):

```bash
curl -u user:PASSWORD -H "Content-Type: application/json" -d '{"url":"https://www.sebi.gov.in/path/to/document.pdf","type":"RHP"}' http://localhost:8080/api/v1/ipos/example-limited/documents
curl -u user:PASSWORD -X POST http://localhost:8080/api/v1/ipos/example-limited/analyze
```

## API

- `GET /api/v1/ipos` — dashboard summaries
- `GET /api/v1/ipos/{slug}` — financial history, market history and analysis
- `GET /api/v1/ipos/compare?ids=1,2` — compare up to four IPOs
- `POST /api/v1/ipos/discover` — run configured source discovery
- `GET /api/v1/ipos/discovery-status` — inspect the latest collector result and per-source errors
- `POST /api/v1/ipos/{slug}/analyze` — queue reproducible analysis
- `POST /api/v1/ipos/{slug}/documents` — download and extract an allowlisted official RHP/DRHP PDF
- `GET /api/v1/ipos/{slug}/recommendation-history` — versioned history
- `GET /api/v1/jobs/{jobId}` — background-job state
- `GET /api/v1/dashboard/summary` — dashboard counts

## Scoring model

The MVP weights financial quality (25%), growth (15%), valuation (20%), business (10%), governance (10%), issue structure (5%), subscription (5%), sentiment (5%), and risk adjustment (5%). Unknown factors receive conservative neutral values and are listed in `missingInformation`. Confidence is based only on coverage of source-backed fields.

## External integration boundary

The collectors use only public official pages and do not bypass WAFs or CAPTCHAs. NSE/BSE can change or restrict their pages, so production deployments should monitor discovery status and use a licensed feed if reliable machine access is required.
