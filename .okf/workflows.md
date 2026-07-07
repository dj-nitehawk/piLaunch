---
type: Playbook
title: Workflows
description: Setup, build, run, verify, release, and local development commands.
tags: [workflows, gradle, ci]
---

# Workflows

## Prerequisites

- JDK 21.
- JetBrains IDE based on IntelliJ Platform `2026.1` or newer for runtime use.
- Bundled JetBrains Terminal plugin enabled.
- `pi` available on `PATH` for plugin runtime behavior.

## Setup

Use the checked-in Gradle wrapper:

```bash
./gradlew --version
```

Gradle downloads IntelliJ Platform dependencies through repositories configured in `settings.gradle.kts`.

## Local development

```bash
./gradlew runIde
```

Runs a sandbox IDE with the plugin loaded.

## Build and package

```bash
./gradlew buildPlugin
```

Produces plugin distributions under `build/distributions/*.zip`.

## Verify plugin compatibility

```bash
./gradlew verifyPlugin
```

Uses IntelliJ Plugin Verifier against the configured Rider platform version.

## CI workflow

Manual workflow: `.github/workflows/build.yml`.

```bash
./gradlew --no-configuration-cache --no-parallel buildPlugin
./gradlew --no-configuration-cache --no-parallel verifyPlugin
```

The workflow disables configuration cache and parallel execution to avoid intermittent `ClosedFileSystemException` during IntelliJ Platform dependency resolution on GitHub runners.

## Release workflow

Manual workflow: `.github/workflows/release.yml`.

- Input `release_type`: `patch`, `minor`, or `major`.
- Reads `pluginVersion` from `gradle.properties`.
- Bumps version, builds plugin, commits `gradle.properties`, tags `v<version>`, pushes to `main`, and creates a GitHub release with `build/distributions/*.zip`.
- Publishing/signing config in Gradle reads environment variables; do not put secret values in docs or OKF.

## Lint/format/code generation

- No dedicated lint, format, or code-generation command is currently documented or configured beyond standard Gradle/IntelliJ plugin tasks.
- If adding one, document the command here and in `README.md` if user-facing.

## Sources

- `README.md`
- `build.gradle.kts`
- `settings.gradle.kts`
- `gradle.properties`
- `.github/workflows/build.yml`
- `.github/workflows/release.yml`
