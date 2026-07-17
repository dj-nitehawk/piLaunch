---
type: Reference
title: Project Overview
description: JetBrains plugin that opens a pinned Pi terminal tab and wires attention notifications plus path hotkeys.
tags: [overview, layout, architecture]
resource: README.md
---

# Project Overview

## Purpose

**piLaunch** (`dev.pilaunch`) is an IntelliJ Platform plugin that opens a pinned editor tab named **Pi**, starts the `pi` CLI in the project base path, and integrates IDE notifications and path-sending actions.

## Scope

- Single Gradle module; Kotlin sources under `src/main/kotlin/dev/pilaunch/`.
- Builds against **Rider** via IntelliJ Platform Gradle (`rider(...)`); platform version and since-build from `gradle.properties`.
- Requires bundled **Terminal** plugin and `pi` on `PATH`.
- MIT licensed. Version from `pluginVersion` in `gradle.properties`.

## Capabilities

| Area | Behavior |
| --- | --- |
| Startup | `OpenPiOnStartupActivity` opens/pins a `PiSessionFile` tab without stealing focus |
| Session | `PiSessionEditor` hosts a local terminal; runs `pi -e <bundled-extension>` with notify env vars |
| Attention | Bundled `pilaunch-attention.ts` POSTs on `agent_end`; IDE balloon + system notification |
| Path links | Terminal output paths like `src/file.ext:10` are clickable (must resolve under project base) |
| Hotkeys | `Alt+P` path; `Alt+L` path+line/selection formats (see README) |

## Layout

| Path | Role |
| --- | --- |
| `src/main/kotlin/dev/pilaunch/` | All plugin Kotlin sources (6 files) |
| `src/main/resources/META-INF/plugin.xml` | Plugin id, deps, extensions, actions |
| `src/main/resources/pi/pilaunch-attention.ts` | Bundled Pi extension (copied to IDE system path at runtime) |
| `src/main/resources/icons/` | Tab/action icons |
| `build.gradle.kts`, `settings.gradle.kts`, `gradle.properties` | Build, platform, version |
| `.github/workflows/` | CI build/verify and release |

## Architecture (folded)

- **Style:** IntelliJ plugin; project-level service + editor provider + startup activity + actions.
- **Components:**
  - `PiSessionFile` / `PiSessionEditorProvider` / `PiSessionEditor` — virtual file + terminal-backed editor (`PiLaunchSession.kt`)
  - `PiLauncher` / `PiTerminalRunner` — shell terminal; cwd = project base path
  - `PiExtensionInstaller` — extracts attention extension to `<IDE system path>/piLaunch/`
  - `PiAttentionNotificationBridge` — loopback HTTP `/notify` with token; 1s debounce; suppress when Pi tab selected and IDE active
  - `PiTerminalFileLinkHandler` — hover/click file refs in terminal buffer
  - `SendToPiAction` subclasses — paste path/location into Pi prompt (`PiLaunchActions.kt`)
- **Boundaries:** no separate modules; session disposable owns terminal/handlers/notify stop.
- **External:** Terminal plugin APIs; shells out to `pi`; HTTP only to `127.0.0.1`.

## Status

Productive plugin with Gradle packaging and GitHub release workflow. No automated tests in-repo.

## Non-goals

- Not a multi-product or multi-plugin workspace (single artifact).
- Does not vendor or ship the `pi` binary.

## Sources
- `README.md`
- `build.gradle.kts`
- `gradle.properties`
- `src/main/resources/META-INF/plugin.xml`
- `src/main/kotlin/dev/pilaunch/`
