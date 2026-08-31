# Architecture

The MVP uses a source-backed calculation pipeline:

```mermaid
flowchart TD
    S[Source adapters] --> P[Normalized IPO data]
    P --> C[Deterministic calculations]
    P --> R[Risk extraction]
    C --> E[Scoring engine]
    R --> E
    E --> A[REST API]
    A --> D[Angular dashboard]
```

The scoring service is intentionally deterministic. A future Spring AI adapter may summarize cited RHP passages, but numeric calculations, hard-risk overrides, confidence, and verdict thresholds remain Java domain logic.

## Data integrity rules

- Missing values remain missing; they are never inferred silently.
- Each market snapshot stores its source URL and observation time.
- Confidence measures evidence coverage, not expected return.
- GMP cannot independently produce an Apply verdict.
- Live source adapters must respect source terms, access controls, and rate limits.

