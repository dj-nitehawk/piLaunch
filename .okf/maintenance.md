---
type: Reference
title: OKF Maintenance
description: Rules for keeping OKF synchronized and conformant.
tags: [okf, maintenance]
---

# OKF Maintenance

Preserve OKF v0.1 conformance:

- Every non-reserved `.md` file needs YAML frontmatter with a non-empty `type` field.
- `index.md` and `log.md` are reserved filenames, not ordinary concept documents.
- Only the bundle-root `.okf/index.md` may include frontmatter, and only for `okf_version`.
- Do not create empty placeholder OKF files.

Update OKF when changing:

- architecture, lifecycle, or plugin component boundaries;
- IDE extension registrations, actions, hotkeys, notification behavior, or public user-visible behavior;
- bridge protocol, security model, environment variables, or runtime process model;
- build, run, verify, release, lint, format, generation, or test commands;
- dependencies, runtime versions, target IntelliJ Platform versions, or package management;
- repository layout, generated files, or files agents should avoid editing;
- testing strategy, test layout, required validation, or manual checklists;
- known gotchas or common failure modes.

If OKF conflicts with code/tests/config:

1. Verify behavior from authoritative project sources.
2. Update the stale OKF file.
3. Mention the correction in the final response.

Before finishing any future task:

- Update `.okf/` if the task affected project knowledge.
- If no OKF update is needed, state that explicitly and briefly explain why.

## Sources

- `.okf/index.md`
- Repository source, manifests, workflows, and README current at OKF setup time.
