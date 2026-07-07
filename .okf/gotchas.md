---
type: Reference
title: Gotchas
description: Practical traps, constraints, and fragile areas agents should avoid.
tags: [gotchas, constraints]
---

# Gotchas

- `pi` is not bundled; runtime launch fails if `pi` is not on `PATH` inside the IDE terminal environment.
- The Terminal plugin is both a Gradle bundled plugin dependency and a `plugin.xml` dependency; keep both aligned.
- CI uses `--no-configuration-cache --no-parallel` to avoid intermittent IntelliJ Platform dependency resolution failures on GitHub runners. Do not remove casually.
- Startup intentionally opens the Pi tab without selecting it; send actions intentionally select/focus it.
- During pi-only startup, `FileEditorManager.selectedFiles` can be empty even though the Pi tab is the only visible editor; notification suppression must handle that fallback.
- Alt+L line numbers are 1-based even though IntelliJ editor positions are 0-based.
- Send actions intentionally mutate the IDE clipboard and leave sent text there.
- The attention bridge must stay loopback-only and token-protected; do not expose it on external interfaces.
- The bundled Pi extension must catch notification failures; Pi should continue if the IDE bridge is gone.
- No automated tests currently exist, so UI/terminal changes need Gradle validation plus manual `runIde` checks when practical.
- Ignored/generated directories (`build/`, `.gradle/`, `.kotlin/`, `.intellijPlatform/`, IDE metadata) are not source of truth and should not be hand-edited.

## Sources

- `README.md`
- `.gitignore`
- `.github/workflows/build.yml`
- `src/main/kotlin/dev/pilaunch/`
- `src/main/resources/pi/pilaunch-attention.ts`
