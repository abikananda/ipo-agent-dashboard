# Requirements coverage

## Implemented

- Normalized IPO, financial, market, valuation, risk, document, source, analysis and job storage
- Deterministic weighted scoring, separate confidence, listing/long-term verdicts and critical-risk override
- Source timestamps, URLs, reliability and document hashes
- Configurable SEBI/NSE/BSE discovery adapters, scheduling and retry
- PDF type/size validation, extraction and document-host allowlist
- Optional strict structured AI narrative adapter; disabled safely without credentials
- Dashboard list, detail retrieval, financial/GMP charts, risk/source display and four-item comparison selection
- OpenAPI, Actuator, Spring Security baseline, Docker, Flyway and CI

## Deployment-dependent

- Live data requires endpoints or licensed feeds that the deployer is authorized to automate.
- Peer prices/sector medians require a market-data provider.
- AI narrative generation requires a configured provider and API key.
- Alert delivery requires an email/SMS/push provider.

These conditions are surfaced as missing data or unconfigured providers. The system never substitutes sample values in the default profile.
