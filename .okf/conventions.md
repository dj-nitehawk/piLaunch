---
type: Reference
title: Conventions
description: Package layout, naming, and plugin patterns used in piLaunch.
tags: [conventions, layout]
---

# Conventions

## Naming and package

- Root package: `dev.pilaunch` (matches `pluginGroup` / plugin id `dev.pilaunch`).
- Kotlin files are feature-oriented top-level types (e.g. `PiLaunchSession.kt` holds session editor stack; `PiLaunchActions.kt` holds send-to-Pi actions).
- User-facing tab name is the constant `Pi` (`PI_TAB_NAME`).
- Notification group id: `piLaunch` (must match `plugin.xml` and bridge constants).

## Style

- Kotlin, JVM target 21; prefer small private helpers over deep hierarchies.
- IntelliJ APIs: prefer `DumbAware` for startup/actions so indexing does not block.
- UI work on EDT via `ApplicationManager.getApplication().invokeLater` with `project.isDisposed` checks.
- Disposables: attach terminal/handlers to session disposable; register cleanup with `Disposer`.

## Plugin registration

- Declare extensions/actions only in `src/main/resources/META-INF/plugin.xml`.
- File editor provider id: `pilaunch.session`.
- Action ids: `dev.pilaunch.SendCurrentFilePathToPi`, `dev.pilaunch.SendCurrentFileLocationToPi`.
- Platform deps: `com.intellij.modules.platform` + `org.jetbrains.plugins.terminal`.

## Errors and validation

- Bundled extension missing → hard `error(...)` in `PiExtensionInstaller`.
- Notify HTTP: method/token checks; best-effort failures in the TS extension (`catch` empty).
- File links resolve only under project base path; absolute paths outside project ignored.
- Shell args quoted with single-quote + escape for env and `-e` path.

## Config and secrets

- Build/publish env names only (never commit values): `CERTIFICATE_CHAIN`, `PRIVATE_KEY`, `PRIVATE_KEY_PASSWORD`, `PUBLISH_TOKEN`, `CHANGE_NOTES`.
- Runtime notify: `PI_LAUNCH_NOTIFY_URL`, `PI_LAUNCH_NOTIFY_TOKEN` injected into the shell command for `pi`.
- Version/platform knobs live in `gradle.properties` (`pluginVersion`, `platformVersion`, `pluginSinceBuild`).

## Sources
- `src/main/kotlin/dev/pilaunch/`
- `src/main/resources/META-INF/plugin.xml`
- `build.gradle.kts`
- `gradle.properties`
