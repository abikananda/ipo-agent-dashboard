# Production configuration

The default profile contains no fabricated IPO records. Configure only feeds you are authorized to automate.

| Variable | Purpose |
|---|---|
| `IPO_SEBI_FEED_URL` | Normalized SEBI IPO discovery feed |
| `IPO_NSE_FEED_URL` | Normalized NSE IPO discovery feed |
| `IPO_BSE_FEED_URL` | Normalized BSE IPO discovery feed |
| `IPO_DOCUMENT_ALLOWED_HOSTS` | Comma-separated HTTPS hosts allowed for document retrieval |
| `IPO_AI_PROVIDER` | `disabled` or `openai-compatible` |
| `IPO_AI_API_KEY` | AI provider secret; never commit it |
| `IPO_AI_MODEL` | Structured-output capable model |

Mutation APIs require HTTP Basic authentication and retain CSRF protection. Configure Spring Security credentials through deployment secrets. Read-only research endpoints are public by design.
