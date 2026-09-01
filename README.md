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
- Configurable SEBI, NSE and BSE JSON feed adapters with retry and source provenance
- RHP/DRHP PDF validation, extraction, hashing and SSRF host allowlisting
- Risk, valuation, source, document, analysis-history and background-job models
- OpenAI-compatible structured narrative provider with a safe disabled fallback
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

Official exchange/regulator endpoints can change access requirements and must be supplied only when their terms permit automated access:

```text
IPO_SEBI_FEED_URL=
IPO_NSE_FEED_URL=
IPO_BSE_FEED_URL=
```

Each configured endpoint must return normalized JSON records containing `id`, `companyName`, `type`, and `status`. Unconfigured sources are reported explicitly and do not generate fake data.

Enable optional structured AI narrative analysis with `IPO_AI_PROVIDER=openai-compatible`, an API key, base URL and model. Numeric formulas, confidence, hard-risk overrides and final score aggregation remain deterministic Java logic.

## API

- `GET /api/v1/ipos` — dashboard summaries
- `GET /api/v1/ipos/{slug}` — financial history, market history and analysis
- `GET /api/v1/ipos/compare?ids=1,2` — compare up to four IPOs
- `POST /api/v1/ipos/discover` — run configured source discovery
- `POST /api/v1/ipos/{slug}/analyze` — queue reproducible analysis
- `GET /api/v1/ipos/{slug}/recommendation-history` — versioned history
- `GET /api/v1/jobs/{jobId}` — background-job state
- `GET /api/v1/dashboard/summary` — dashboard counts

## Scoring model

The MVP weights financial quality (25%), growth (15%), valuation (20%), business (10%), governance (10%), issue structure (5%), subscription (5%), sentiment (5%), and risk adjustment (5%). Unknown factors receive conservative neutral values and are listed in `missingInformation`. Confidence is based only on coverage of source-backed fields.

## External integration boundary

The repository implements the collection contracts, scheduling, persistence, validation and failure behavior. It intentionally does not ship undocumented scrapers, CAPTCHA bypasses, paid-feed credentials, or invented endpoint URLs. Production deployment must configure legally accessible source feeds and validate their payload mappings.
