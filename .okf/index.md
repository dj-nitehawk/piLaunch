---
okf_version: "0.1"
---

# OKF Knowledge Set

This directory contains compact operational knowledge for agents working in `piLaunch`, a JetBrains IDE plugin that opens and controls a pinned Pi terminal tab. Read relevant files before editing. Keep OKF synchronized with code, tests, docs, and configuration.

## Core reading order

- [Project Overview](project-overview.md) - Project purpose, scope, users, and capabilities.
- [Architecture](architecture.md) - Plugin components, runtime flow, security boundaries, and invariants.
- [Code Map](code-map.md) - Repository layout and where behavior lives.
- [Conventions](conventions.md) - Coding, API, configuration, and design conventions.

## Workflow and validation

- [Workflows](workflows.md) - Build, run, verify, release, and developer commands.
- [Testing](testing.md) - Current test state and validation expectations.

## Task-specific references

- [Dependencies](dependencies.md) - Gradle, Kotlin, IntelliJ Platform, Terminal plugin, and runtime versions.
- [Operations](operations.md) - Runtime behavior, localhost bridge, notifications, config, and publishing inputs.
- [Gotchas](gotchas.md) - Non-obvious constraints and common mistakes.
- [Maintenance](maintenance.md) - OKF update and conformance rules.

## Authority rule

If OKF conflicts with source code, tests, generated artifacts, or project manifests, verify current behavior from those authoritative sources, then update OKF.

## Maintenance rule

Before finishing work, update OKF if the change affects architecture, behavior, commands, dependencies, tests, deployment, or conventions. If no update is needed, state why in the final response.
