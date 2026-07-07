---
type: Reference
title: Operations
description: Runtime processes, notification bridge behavior, configuration, release inputs, and operational caveats.
tags: [operations, runtime, release]
---

# Operations

## Runtime process model

- The plugin runs inside the JetBrains IDE process.
- Each Pi tab hosts a JetBrains Terminal widget.
- The terminal command clears the terminal, exports notification bridge environment variables, and runs `pi -e <attention-extension>`.
- The terminal working directory is the project base path when available.

## Attention bridge

- `PiAttentionNotificationBridge` is a project-level service.
- It creates one loopback HTTP server per active bridge and reuses it while server/token exist.
- The server binds to an ephemeral port on the loopback interface.
- Endpoint path is `/notify`; valid requests are `POST` with `x-pilaunch-token`.
- Responses: `204` on accepted event, `401` on token mismatch, `405` on non-POST, `500` on unhandled processing error before response.
- The bridge is best-effort: the Pi extension catches fetch failures so IDE shutdown or bridge disposal does not break Pi.

## Notifications

- Notification group id: `piLaunch`.
- User-visible notification title: `Pi needs attention`.
- IDE notification type: information balloon.
- Accepted events also request a native system notification with body text `Pi finished work in <project name>.` when supported by the OS and IDE notification settings.
- Notifications are suppressed while the Pi tab is selected.
- If IntelliJ reports no selected files during pi-only startup, the bridge treats a sole open Pi tab as selected for suppression.
- Active IDE bubble notifications expire when the Pi tab is selected again; native system notification lifetime is controlled by the OS.
- Notification creation is debounced for 1 second.

## Configuration and environment variables

Runtime variables passed only to the launched Pi command:

- `PI_LAUNCH_NOTIFY_URL`
- `PI_LAUNCH_NOTIFY_TOKEN`

Build/publishing variables read by Gradle:

- `CHANGE_NOTES`
- `CERTIFICATE_CHAIN`
- `PRIVATE_KEY`
- `PRIVATE_KEY_PASSWORD`
- `PUBLISH_TOKEN`

Never copy secret values into OKF, logs, commits, or release notes.

## CI/release operations

- CI build/verify workflow is manual and runs on Ubuntu with Temurin JDK 21.
- Release workflow is manual, bumps semantic version in `gradle.properties`, commits/tags on `main`, and uploads `build/distributions/*.zip` to a GitHub release.
- Release workflow has `contents: write` permission.

## Sources

- `src/main/kotlin/dev/pilaunch/PiLaunchSession.kt`
- `src/main/kotlin/dev/pilaunch/PiAttentionNotificationBridge.kt`
- `src/main/resources/pi/pilaunch-attention.ts`
- `src/main/resources/META-INF/plugin.xml`
- `build.gradle.kts`
- `.github/workflows/build.yml`
- `.github/workflows/release.yml`
