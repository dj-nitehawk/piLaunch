# piLaunch

JetBrains IDE plugin that opens a pinned **Pi** editor tab for the current project and starts `pi` in it.

The plugin targets Rider/IntelliJ Platform `2026.1` (`since-build 261`) and uses the bundled JetBrains Terminal plugin.

## Requirements

- JetBrains IDE based on IntelliJ Platform `2026.1` or newer
- Bundled **Terminal** plugin enabled
- `pi` available on `PATH`
- JDK 21 when building from source

## What it does

- Opens a pinned editor tab named **Pi** when a project opens.
- Starts Pi in the tab and loads a bundled Pi extension for lifecycle notifications.
- Uses the project base path as the terminal working directory.
- Shows `Pi needs attention` IDE and system notifications when Pi finishes a prompt and returns to idle.
- Suppresses those notifications while the **Pi** tab is selected and the Rider window is active.
- Still shows notifications if Rider is not the active window, even when the **Pi** tab is selected.
- Closes active Pi attention IDE notifications when the **Pi** tab is selected again or Rider is reactivated with **Pi** selected.
- Reuses the plugin's **Pi** tab for send actions.
- Sends file/path text by copying it to the IDE clipboard, focusing the plugin's **Pi** tab, and pasting into the terminal.
- The clipboard is left containing the sent text.

## Attention notifications

piLaunch starts a loopback-only localhost bridge and launches Pi with a bundled extension. The extension listens for Pi's `agent_end` event, so notifications are based on Pi finishing work rather than file changes.

Notifications:

- Appear after Pi finishes responding, including prompts that do not modify files.
- Do not appear for manual file edits.
- Do not appear while the **Pi** tab is currently selected and the Rider window is active.
- Still appear if Rider is not the active window, even when the **Pi** tab is selected.
- Include a Rider/IDE bubble and a native system notification when supported by the OS and IDE notification settings.
- Active IDE bubbles are dismissed automatically when the **Pi** tab is selected again or Rider is reactivated with **Pi** selected; system notification lifetime is controlled by the OS.

The bridge binds only to loopback and requires a random per-session token.

## Hotkeys

| Hotkey | Action | Sent text |
| --- | --- | --- |
| `Alt+P` | Send Current File Path to Pi | Relative path from project root, or absolute path if no project base path is available. |
| `Alt+L` | Send Current File Location to Pi | Current file path plus line information. |

`Alt+L` formats text as follows:

- Caret with no selection: `path/to/file.ext:line`
- Multi-line selection: `path/to/file.ext:startLine-endLine`
- Single-symbol selection: `symbol (path/to/file.ext:line)`

Line numbers are 1-based.

## Building from source

```bash
./gradlew runIde        # launch a sandbox IDE with the plugin
./gradlew buildPlugin   # produce build/distributions/*.zip
./gradlew verifyPlugin  # run IntelliJ Plugin Verifier
```
