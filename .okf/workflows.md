---
type: Playbook
title: Workflows
description: Setup, build, run sandbox IDE, verify, and release commands for piLaunch.
tags: [build, ops]
resource: README.md
---

# Workflows

## Setup

- JDK **21** required.
- Use Gradle wrapper (`./gradlew`); distribution from `gradle/wrapper` (Gradle 9.6.0).
- `pi` must be on `PATH` for runtime; Terminal plugin must be enabled in the target IDE.
- Local secrets: use env vars for signing/publish; do not put values in repo (see `.gitignore` for `.env`, `local.properties`).

## Build and run

| Command | Purpose |
| --- | --- |
| `./gradlew runIde` | Sandbox IDE with the plugin |
| `./gradlew buildPlugin` | Plugin zip under `build/distributions/` |
| `./gradlew verifyPlugin` | IntelliJ Plugin Verifier (RD / `platformVersion`) |

Gradle properties of note: configuration cache and build cache enabled in `gradle.properties`; CI deliberately passes `--no-configuration-cache --no-parallel` to avoid intermittent platform dependency resolution failures.

## Lint and format

No dedicated format/lint Gradle tasks documented or configured in-repo. Follow existing Kotlin style in `src/main/kotlin/dev/pilaunch/`.

## Codegen and migrations

None. Bundled TS extension is a static resource; at runtime it is copied (overwrite) to `<PathManager system path>/piLaunch/pilaunch-attention.ts`.

## Testing

No `src/test` or automated test suite present. Validation is manual via `runIde` plus CI `buildPlugin` / `verifyPlugin`.

## CI and release

- `.github/workflows/build.yml`: `workflow_dispatch`; JDK 21 Temurin; `buildPlugin` then `verifyPlugin`.
- `.github/workflows/release.yml`: `workflow_dispatch` with patch/minor/major; bumps `pluginVersion` in `gradle.properties`; builds zip; commits, tags `v*`, GitHub Release with distribution zip.

## Dependencies (folded)

| Item | Detail |
| --- | --- |
| Language/runtime | Kotlin JVM, Java 21 |
| Build | Gradle wrapper + IntelliJ Platform Gradle Plugin `2.16.0` (`settings.gradle.kts`) |
| Plugins | `org.jetbrains.kotlin.jvm` `2.4.0`, `org.jetbrains.intellij.platform`, `org.jetbrains.changelog` `2.2.1` |
| Platform | `rider(platformVersion)` with `useInstaller = false`; bundled `org.jetbrains.plugins.terminal` |
| Config keys | `pluginGroup`, `pluginName`, `pluginVersion`, `pluginSinceBuild`, `platformVersion` in `gradle.properties` |
| kotlin.stdlib.default.dependency | `false` (platform-provided) |

## Sources
- `README.md`
- `build.gradle.kts`
- `settings.gradle.kts`
- `gradle.properties`
- `.github/workflows/build.yml`
- `.github/workflows/release.yml`
