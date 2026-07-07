---
type: Architecture
title: Architecture
description: Plugin components, runtime flow, communication, security, persistence, and invariants.
tags: [architecture, boundaries]
---

# Architecture

## Style

Single-module Kotlin JetBrains plugin. The IDE hosts all plugin code; Pi runs inside a JetBrains Terminal widget opened as a custom virtual file editor.

## Components

| Component | Location | Responsibility |
| --- | --- | --- |
| Startup activity | `OpenPiOnStartupActivity.kt` | Opens/reuses and pins the Pi editor tab after project startup without selecting it. |
| Session virtual file/editor | `PiLaunchSession.kt` | Defines `PiSessionFile`, file type, editor provider, editor view, and terminal launch path. |
| Terminal launcher | `PiLaunchSession.kt` | Starts shell terminal with project base path, starts notification bridge, and executes `pi -e <extension>`. |
| Attention bridge | `PiAttentionNotificationBridge.kt` | Project service hosting a loopback HTTP endpoint for Pi completion events and IDE notifications. |
| Send actions | `PiLaunchActions.kt` | Implements Alt+P/Alt+L actions that focus the Pi tab and write context text to the terminal. |
| Bundled Pi extension | `src/main/resources/pi/pilaunch-attention.ts` | Listens for Pi `agent_end` and POSTs to the IDE bridge. |
| Plugin descriptor | `src/main/resources/META-INF/plugin.xml` | Registers editor provider, startup activity, notification group, Terminal dependency, and actions. |

## Runtime flow

1. Project opens.
2. `OpenPiOnStartupActivity` creates or reuses `PiSessionFile(project.basePath)` and opens it pinned as **Pi**.
3. `PiSessionEditor` creates `PiSessionView`.
4. `PiLauncher.launch` starts a Terminal widget with `workingDirectory = project.basePath`.
5. `PiAttentionNotificationBridge.start` creates/reuses a loopback HTTP `/notify` endpoint with a random per-session token.
6. Launcher sends `clear && PI_LAUNCH_NOTIFY_URL=... PI_LAUNCH_NOTIFY_TOKEN=... pi -e <bundled-extension>` to the terminal.
7. The bundled Pi extension sends a POST on `agent_end`; the bridge creates an IDE `Pi needs attention` notification and requests a native system notification unless the Pi tab is selected.

## Dependency rules

- Plugin code depends on IntelliJ Platform and the bundled Terminal plugin APIs.
- Runtime behavior assumes `pi` is available on `PATH`; the plugin does not vendor or install Pi.
- The bundled TypeScript Pi extension should remain small and best-effort; it must not break Pi if the IDE bridge is unavailable.

## Communication and security rules

- The attention bridge binds only to loopback (`127.0.0.1` via `InetAddress.getLoopbackAddress()`) on an ephemeral port.
- Bridge requests must be `POST` and include `x-pilaunch-token`; invalid methods/tokens return `405`/`401`.
- Token is random, per bridge start, and passed through environment variables only for the launched `pi` process.
- Do not log, persist, or document actual token values.

## Persistence rules

- No application data persistence is evident.
- `PiSessionFile` is a virtual editor file; the terminal session and bridge are disposed with the editor/session/project lifecycle.
- Clipboard is intentionally changed by send actions and left containing sent text.

## Invariants

- The editor tab name remains `Pi` and should be pinned/non-preview when opened.
- Startup opens the Pi tab without selecting/focusing it; send actions select and focus it.
- Suppress attention notifications while the Pi tab is selected and dismiss active IDE bubbles when selected.
- Do not replace the loopback/token bridge with a wider network listener.
- Keep Terminal plugin dependency registered in `plugin.xml` and Gradle.

## Sources

- `README.md`
- `src/main/kotlin/dev/pilaunch/OpenPiOnStartupActivity.kt`
- `src/main/kotlin/dev/pilaunch/PiLaunchSession.kt`
- `src/main/kotlin/dev/pilaunch/PiAttentionNotificationBridge.kt`
- `src/main/kotlin/dev/pilaunch/PiLaunchActions.kt`
- `src/main/resources/pi/pilaunch-attention.ts`
- `src/main/resources/META-INF/plugin.xml`
