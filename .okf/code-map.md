---
type: Reference
title: Code Map
description: Repository layout, important files, generated outputs, and edit guidance.
tags: [code-map, navigation]
---

# Code Map

## Top-level layout

| Path | Purpose |
| --- | --- |
| `README.md` | User-facing feature summary, requirements, hotkeys, and build commands. |
| `build.gradle.kts` | Kotlin/JVM and IntelliJ Platform Gradle plugin configuration. |
| `settings.gradle.kts` | Root project name and repository/plugin management. |
| `gradle.properties` | Plugin metadata, IntelliJ Platform target, Kotlin/Gradle settings. |
| `gradle/wrapper/` | Gradle wrapper, currently Gradle `9.6.0`. |
| `.github/workflows/build.yml` | Manual CI build and verifier workflow. |
| `.github/workflows/release.yml` | Manual version bump, build, tag, and GitHub release workflow. |
| `src/main/kotlin/dev/pilaunch/` | Kotlin plugin implementation. |
| `src/main/resources/META-INF/plugin.xml` | IntelliJ plugin registrations. |
| `src/main/resources/pi/pilaunch-attention.ts` | Bundled Pi extension loaded with `pi -e`. |
| `src/main/resources/icons/` and `pluginIcon*.svg` | Plugin/action icons. |
| `.okf/` | Agent operational knowledge set. |

## Kotlin source map

| File | Purpose |
| --- | --- |
| `OpenPiOnStartupActivity.kt` | Project startup tab opening/pinning. |
| `PiLaunchSession.kt` | `PiSessionFile`, file type, editor provider/editor/view, Terminal runner, command launch. |
| `PiAttentionNotificationBridge.kt` | Project service for localhost bridge and IDE notifications. |
| `PiExtensionInstaller.kt` | Resolves bundled attention extension resource path. |
| `PiLaunchActions.kt` | Alt+P/Alt+L action formatting and terminal writing. |

## Tests

- No `src/test` tree or test files are currently present.
- Validation relies on Gradle build/plugin tasks unless tests are added.

## Generated/ignored paths

Do not edit generated/cache outputs manually:

- `build/`
- `.gradle/`
- `.kotlin/`
- `.intellijPlatform/`
- `.idea/`, `out/`, IDE workspace files
- `.gradle-user-home/`

Keep `gradle/wrapper/gradle-wrapper.jar` committed; `.gitignore` explicitly allows it.

## Edit guidance

- Register new IDE extension points/actions in `src/main/resources/META-INF/plugin.xml`.
- Keep plugin implementation under `src/main/kotlin/dev/pilaunch/`.
- Keep bundled Pi extension resources under `src/main/resources/pi/`.
- If adding tests, prefer the standard Gradle/Kotlin test layout under `src/test/` and document commands in OKF.

## Sources

- `.gitignore`
- `README.md`
- `build.gradle.kts`
- `settings.gradle.kts`
- `src/main/`
- `.github/workflows/`
