# IPO Analysis Agent

An explainable Indian IPO research MVP that combines financial quality, issue structure, GMP and subscription demand into separate attractiveness and confidence scores.

> Research assistance only—not personalized financial advice. GMP is unofficial and listing gains are never guaranteed. The included companies and figures are clearly marked demo data.

## What is implemented

- Interactive Angular dashboard with search, status filters and responsive IPO cards
- Detailed recommendation drawer with factor-level scores and explanations
- Spring Boot REST API and OpenAPI UI
- Deterministic scoring engine; the LLM is not allowed to invent or calculate financial values
- Separate overall score and data-confidence score
- Financial history and time-stamped GMP/subscription snapshots with provenance
- MySQL schema and demo data through Flyway
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

## API

- `GET /api/v1/ipos` — dashboard summaries
- `GET /api/v1/ipos/{slug}` — financial history, market history and analysis

## Scoring model

The MVP weights financial quality (25%), growth (15%), valuation (20%), business (10%), governance (10%), issue structure (5%), subscription (5%), sentiment (5%), and risk adjustment (5%). Unknown factors receive conservative neutral values and are listed in `missingInformation`. Confidence is based only on coverage of source-backed fields.

## Roadmap

1. Official NSE/BSE/SEBI source adapters and scheduled collection
2. RHP/DRHP PDF extraction with page citations
3. Peer valuation service and configurable weights
4. Spring AI provider abstraction for narrative risk classification
5. Authentication, watchlists, alerts and recommendation history

