---
type: Reference
title: Gotchas
description: Non-obvious traps for piLaunch plugin development and runtime.
tags: [gotcha, maintain]
---

# Gotchas

- Platform dependency is **Rider** (`rider(...)` in `build.gradle.kts`), not a generic multi-product matrix; plugin verifier uses `RD` + `platformVersion`. README wording is broader (“JetBrains IDE”)—prefer the Gradle target when changing platform setup.
- `since-build` is `261` (2026.1 line); `untilBuild` is open (`null`). Bumping platform requires coordinated `platformVersion` / `pluginSinceBuild` and verifier IDEs.
- Runtime needs **`pi` on PATH** and Terminal plugin; missing either fails at use time, not at compile time.
- Attention bridge binds **loopback only**, random port, token in `x-pilaunch-token`; extension fails soft if IDE is gone.
- Notifications suppressed only when **Pi tab selected and IDE window active**; inactive window still notifies.
- Notification debounce: 1s (`NOTIFICATION_DEBOUNCE_MILLIS`).
- `PiTerminalFileLinkHandler` uses reflection on `TerminalPanel.panelToCharCoords`—platform terminal API drift can silently disable links if the method disappears.
- File links require a project-relative path under the working directory; paths outside the project root are ignored.
- Extension resource path is fixed: `pi/pilaunch-attention.ts` → system dir `piLaunch/`; always overwritten on launch.
- Shell command prefixes with `clear &&` and sets `PI_LAUNCH_NOTIFY_URL` / `PI_LAUNCH_NOTIFY_TOKEN` for that process only.
- Send-to-Pi pastes via clipboard + `ttyConnector.write`; needs an existing or newly opened `PiSessionEditor`.
- Do not commit secrets; signing/publish use env vars only. `.serena/` and build caches are gitignored.
- CI: use `--no-configuration-cache --no-parallel` on GitHub runners for platform resolution stability even though local `gradle.properties` enables configuration cache.
- No automated tests—behavior changes need manual `runIde` checks (startup pin, notify, hotkeys, file links).

## When to update OKF

Sync `.okf/` when changing architecture/boundaries, plugin.xml contracts/actions/hotkeys, platform/deps/version commands, CI/release, notify/extension protocol, or conventions. If unaffected, say so before finishing. Full rules: `AGENTS.md` + okf-setup skill.

## Sources
- `build.gradle.kts`
- `src/main/kotlin/dev/pilaunch/PiAttentionNotificationBridge.kt`
- `src/main/kotlin/dev/pilaunch/PiTerminalFileLinkHandler.kt`
- `src/main/resources/pi/pilaunch-attention.ts`
- `.github/workflows/build.yml`
