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
- Install Pi terminal file link hover/click handling through the underlying `JBTerminalWidget` when the `TerminalWidget` facade lacks the needed terminal panel access.
- Resolve terminal file links relative to the Pi terminal working directory and only navigate to regular files under that directory.
- Do not use terminal message-filter hyperlink rendering for Pi file links because it repaints link backgrounds instead of preserving Pi block backgrounds.

## Notifications and bridge

- Attention notifications use notification group id `piLaunch` and title `Pi needs attention`.
- Show both a Rider/IDE information bubble and a native system notification request for accepted attention events.
- Suppress notifications if the Pi tab is selected and the Rider project window is active.
- Still show notifications if the Rider project window is inactive, even when Pi is selected.
- Expire active IDE bubble notifications when the Pi tab becomes selected, Rider reactivates with Pi selected, or the bridge is disposed.
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
