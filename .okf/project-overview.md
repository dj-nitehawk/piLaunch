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
- Targets **Rider** via IntelliJ Platform Gradle (`rider` dependency); platform version and since-build come from `gradle.properties`.
- Requires bundled **Terminal** plugin and `pi` on `PATH`.
- MIT licensed. Plugin version currently from `pluginVersion` in `gradle.properties`.

## Capabilities

| Area | Behavior |
| --- | --- |
| Startup | `OpenPiOnStartupActivity` opens/pins a `PiSessionFile` tab without stealing focus |
| Session | `PiSessionEditor` hosts a local terminal; runs `pi -e <bundled-extension>` with notify env vars |
| Attention | Bundled `pilaunch-attention.ts` POSTs on `agent_end`; IDE balloon + system notification |
| Path links | Terminal output paths like `src/file.ext:10` are clickable (project-relative only) |
| Hotkeys | `Alt+P` path; `Alt+L` path+line/selection formats (see README) |

## Layout

| Path | Role |
| --- | --- |
| `src/main/kotlin/dev/pilaunch/` | All plugin Kotlin sources |
| `src/main/resources/META-INF/plugin.xml` | Plugin id, deps, extensions, actions |
| `src/main/resources/pi/pilaunch-attention.ts` | Bundled Pi extension (copied to IDE system path at runtime) |
| `src/main/resources/icons/` | Tab/action icons |
| `build.gradle.kts`, `settings.gradle.kts`, `gradle.properties` | Build, platform, version |
| `.github/workflows/` | CI build/verify and release |

## Architecture (folded)

- **Style:** IntelliJ plugin; project-level service + editor provider + startup activity + actions.
- **Components:**
  - `PiSessionFile` / `PiSessionEditorProvider` / `PiSessionEditor` — virtual file + terminal-backed editor
  - `PiLauncher` / `PiTerminalRunner` — shell terminal with working directory = project base path
  - `PiExtensionInstaller` — extracts attention extension to `<IDE system path>/piLaunch/`
  - `PiAttentionNotificationBridge` — loopback HTTP `/notify` with token; debounce; suppress when Pi tab selected and IDE active
  - `PiTerminalFileLinkHandler` — mouse hover/click file refs in terminal buffer
  - `SendToPiAction` subclasses — paste path/location into Pi prompt
- **Boundaries:** no separate modules; UI/editor lifecycle owns terminal disposable; notify server stops with session dispose.
- **External:** depends on Terminal plugin APIs; shells out to `pi`; HTTP only to 127.0.0.1.

## Status

Productive plugin source with Gradle packaging and GitHub release workflow. No automated tests in-repo.

## Non-goals

- Not a general multi-IDE monorepo or multi-plugin workspace (single artifact).
- Does not vendor or ship the `pi` binary.

## Sources
- `README.md`
- `build.gradle.kts`
- `gradle.properties`
- `src/main/resources/META-INF/plugin.xml`
- `src/main/kotlin/dev/pilaunch/`
