---
type: Reference
title: Testing
description: Current test layout, validation commands, and expectations for new tests.
tags: [testing, validation]
---

# Testing

## Current state

- No `src/test/` tree or test files are currently present.
- No separate test framework dependency is configured in `build.gradle.kts`.
- Validation currently centers on building the plugin and running IntelliJ Plugin Verifier.

## Validation commands

Run relevant Gradle tasks after changes:

```bash
./gradlew buildPlugin
./gradlew verifyPlugin
```

For parity with CI, especially when investigating CI-only issues:

```bash
./gradlew --no-configuration-cache --no-parallel buildPlugin
./gradlew --no-configuration-cache --no-parallel verifyPlugin
```

## When adding tests

- Use standard Gradle/Kotlin test layout unless the repository introduces a different convention.
- Document new test dependencies and commands in `build.gradle.kts`, this file, and `workflows.md`.
- Cover formatting behavior for Alt+L/Alt+P if extracting it into testable units.
- Cover notification/bridge behavior carefully if adding integration tests; avoid binding non-loopback interfaces.

## Manual behavior checks

When practical after UI/runtime changes, use `./gradlew runIde` and verify:

- Pi tab opens pinned and non-preview on project startup.
- `pi` starts in the project base path.
- Alt+P sends the current file path.
- Alt+L sends correct 1-based line or selection text.
- Attention IDE bubble and native system notification appear after Pi `agent_end` only when Pi tab is not selected.
- Active IDE bubbles expire when selecting the Pi tab; native system notification lifetime is OS-controlled.

## Sources

- `README.md`
- `build.gradle.kts`
- `.github/workflows/build.yml`
- `src/main/kotlin/dev/pilaunch/`
