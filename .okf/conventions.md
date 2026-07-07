---
type: Reference
title: Conventions
description: Coding, configuration, action, notification, and design conventions for piLaunch.
tags: [conventions, kotlin, intellij]
---

# Conventions

## Kotlin and style

- Kotlin/JVM source targets Java 21 / JVM 21.
- Keep implementation simple and direct; current code uses small classes/objects rather than extra abstraction layers.
- Prefer IntelliJ Platform lifecycle primitives (`Disposable`, `Disposer`, project services, message bus listeners) for resource management.
- Use `ApplicationManager.getApplication().invokeLater` before IDE UI interactions when following existing patterns.
- Use `DumbAware` for actions/startup behavior that must work during indexing, matching existing code.

## IntelliJ Platform patterns

- Register extension points, startup activities, notification groups, and actions in `META-INF/plugin.xml`.
- `PiSessionFile` should be treated as the marker for the plugin-owned Pi tab.
- Set `FileEditorManagerKeys.FORBID_PREVIEW_TAB` and pin the Pi file when opening it.
- Startup opens the Pi tab with `selectAsCurrent = false`; send actions use `selectAsCurrent = true` and `requestFocus = true`.

## Terminal and Pi command patterns

- Start the Terminal widget through the bundled Terminal plugin API.
- Use the project base path as terminal working directory when available.
- Quote shell values with the existing `shellQuote` helper when constructing the `pi -e` command.
- Keep the attention extension loaded through `PiExtensionInstaller.attentionExtensionPath()`.

## Notifications and bridge

- Attention notifications use notification group id `piLaunch` and title `Pi needs attention`.
- Suppress notifications if the Pi tab is selected.
- Expire active notifications when the Pi tab becomes selected or the bridge is disposed.
- Keep notification callbacks debounced (`1_000ms` currently) unless behavior changes are intentional and documented.

## Actions and text formatting

- Alt+P sends the current file display path.
- Alt+L sends `path:line`, `path:startLine-endLine`, or `symbol (path:line)` for a single-symbol selection.
- Line numbers sent to Pi are 1-based.
- Ignore directories and the Pi tab itself as send-action sources.
- Send actions intentionally copy sent text to the IDE clipboard and write it to the terminal connector.

## Configuration and secrets

- Public config lives in `gradle.properties` and Gradle build files.
- CI/publishing secrets are environment variables: `CERTIFICATE_CHAIN`, `PRIVATE_KEY`, `PRIVATE_KEY_PASSWORD`, `PUBLISH_TOKEN`, and release/build workflow inputs.
- Do not commit local `.env`, `*.local`, `local.properties`, or actual secret values.

## Sources

- `build.gradle.kts`
- `gradle.properties`
- `src/main/kotlin/dev/pilaunch/`
- `src/main/resources/META-INF/plugin.xml`
- `.gitignore`
