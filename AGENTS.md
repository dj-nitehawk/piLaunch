# Agent instructions

## OKF knowledge set

This repository uses `.okf/` as compact operational memory for AI agents.

OKF v0.1: non-reserved `.md` files need YAML frontmatter with non-empty `type`, `title`, and `description`; `index.md` is a directory listing; only bundle-root `index.md` may have frontmatter (`okf_version`).

Normative OKF use/update gates live in this file. `.okf/index.md` and the gotchas update reminder are secondary; the okf-setup skill is setup/maintain procedure.

### Before work

Match OKF depth to blast radius:

- Local/small change: `.okf/index.md` + conventions/gotchas (and workflows if commands/build surface is involved).
- Cross-cutting, plugin.xml contracts/actions, notify/extension protocol, platform/deps, or new surface: core set (overview, conventions, workflows, gotchas).

OKF guides. It does not replace checking source, tests, or manifests for exact behavior.

### During work

Preserve conventions, boundaries, and workflows from OKF. On conflict with source/tests/generated artifacts/manifests: prefer verified current behavior, update OKF, mention the correction.

### Before finishing

Sync `.okf/` when the change hits the update triggers listed in `.okf/gotchas.md` (Small shape has no `maintenance.md`). Default trigger inventory: architecture/boundaries; public APIs/routes/schemas/events/contracts; persistence/migrations; deps/runtime; build/run/test/lint/format/generate/deploy; testing strategy; security/auth; config/env/ports/ops; conventions/layout; gotchas. For this plugin that includes `plugin.xml` actions/hotkeys, notify/extension protocol, and CI/release.

If no update needed, state why (pure comment/typo/formatting: `OKF unaffected (non-behavioral edit)`). Task is incomplete until OKF is synced or explicitly unaffected.

### General

Subject to project conventions in OKF/`conventions.md` and this file:

- Focused, minimal changes; prefer existing patterns.
- Do not use em or en dashes as prose punctuation. Use commas, parentheses, or separate sentences instead. Hyphens remain valid in compound words, bullets, code, commands, CLI flags, identifiers, paths, versions, ranges, quotations, and exact terms.
- Do not hand-edit generated artifacts (Gradle/`build/` outputs, `.intellijPlatform/` caches); regenerate via project commands instead.
- Bundled `src/main/resources/pi/pilaunch-attention.ts` is source (edit in place); runtime copy under IDE system path is overwritten on launch.
- If behavior changes, run the smallest relevant command (`./gradlew buildPlugin`, `verifyPlugin`, or `runIde`). If not run, state the blocker.
