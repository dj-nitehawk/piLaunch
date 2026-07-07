# piLaunch

JetBrains IDE plugin that opens a pinned **Pi** editor tab for the current project and starts `pi` in it.

The plugin targets IntelliJ Platform `2026.1` (`since-build 261`) and uses the bundled JetBrains Terminal plugin.

## Requirements

- JetBrains IDE based on IntelliJ Platform `2026.1` or newer
- Bundled **Terminal** plugin enabled
- `pi` available on `PATH`
- JDK 21 when building from source

## What it does

- Opens a pinned editor tab named **Pi** when a project opens.
- Starts Pi in the tab and loads a bundled Pi extension for lifecycle notifications.
- Uses the project base path as the terminal working directory.
- Lets you hover and click project-relative terminal output paths like `src/file.ext` and `src/file.ext:10` to easily navigate to that file.
- Shows `Pi needs attention` IDE and system notifications when Pi finishes a prompt and returns to idle.
- Suppresses those notifications while the **Pi** tab is selected and the IDE window is active.
- Still shows notifications if the IDE is not the active window, even when the **Pi** tab is selected.
- Closes active Pi attention IDE notifications when the **Pi** tab is selected again or IDE is reactivated with **Pi** selected.
- Sends file/paths to the plugin's **Pi** tab with hotkeys.

## Attention notifications

The plugin listens for Pi's `agent_end` event, so notifications are based on Pi finishing work.

Notifications:

- Appear after Pi finishes responding, including prompts that do not modify files.
- Do not appear while the **Pi** tab is currently selected and the IDE window is active.
- Still appear if the IDE is not the active window, even when the **Pi** tab is selected.
- Include a IDE bubble and a native system notification when supported by the OS and IDE notification settings.
- Active IDE bubbles are dismissed automatically when the **Pi** tab is selected again or the IDE is reactivated with **Pi** selected.

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
