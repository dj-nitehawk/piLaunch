---
type: Reference
title: Project Overview
description: Purpose, scope, capabilities, users, and terms for the piLaunch JetBrains plugin.
tags: [overview, product]
---

# Project Overview

## Purpose

`piLaunch` is a JetBrains IDE plugin that opens a pinned editor tab named **Pi** for the current project and starts the `pi` coding agent inside that tab.

## Scope

Current plugin behavior:

- Opens/reuses a pinned **Pi** editor tab when a project opens.
- Starts a terminal session in the project base path and runs `pi` with a bundled Pi extension.
- Uses a loopback-only notification bridge so Pi can notify the IDE after `agent_end`.
- Shows `Pi needs attention` IDE and system notifications when Pi finishes work and either the Pi tab is not selected or the Rider window is inactive.
- Dismisses active Pi attention IDE notifications when the Pi tab becomes selected or Rider is reactivated with Pi selected.
- Provides actions/hotkeys to send the current file path or file location to the Pi prompt.

## Users and consumers

- JetBrains IDE users, especially Rider/IntelliJ Platform `2026.1` or newer users who want Pi embedded in their IDE.
- The plugin consumes IntelliJ Platform APIs, the bundled Terminal plugin, and the locally installed `pi` CLI.

## Maturity/status

- Plugin version: `1.2.0` in `gradle.properties`.
- Targets IntelliJ Platform/Rider `2026.1.3` with since-build `261`.
- CI and release workflows are manual (`workflow_dispatch`).

## Glossary

- **Pi tab**: Pinned editor tab backed by `PiSessionFile`, displayed as `Pi`.
- **Pi session**: Terminal-backed editor created by `PiSessionEditor`/`PiSessionView`.
- **Attention bridge**: Project-level localhost HTTP bridge that receives Pi extension callbacks and creates IDE notifications.
- **Bundled extension**: `src/main/resources/pi/pilaunch-attention.ts`, loaded into Pi with `pi -e`.

## Sources

- `README.md`
- `gradle.properties`
- `src/main/resources/META-INF/plugin.xml`
- `src/main/kotlin/dev/pilaunch/`
