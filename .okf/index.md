---
okf_version: "0.1"
---

# OKF Knowledge Set

Compact operational knowledge for agents. Read relevant files before editing. Keep synchronized with code, tests, docs, and configuration.

## Core reading order
* [Project Overview](project-overview.md) — purpose, scope, layout, boundaries
* [Conventions](conventions.md) — coding/design rules
* [Workflows](workflows.md) — build, run, verify, release
* [Gotchas](gotchas.md) — traps and when to update OKF

## Authority
If OKF conflicts with source, tests, generated artifacts, or manifests: verify those, then update OKF.

## Maintenance
Normative OKF use/update gates: repo canonical agent instructions (`AGENTS.md`). Short update reminder lives in [Gotchas](gotchas.md) (`maintenance.md` absent for Small shape).
Before finishing, sync OKF when triggers apply; if not needed, state why (`OKF unaffected (non-behavioral edit)` for pure comment/typo/format).
