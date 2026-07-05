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
- Starts Pi with:

```bash
clear && pi
```

- Uses the project base path as the terminal working directory.
- Reuses the plugin's **Pi** tab for send actions.
- Sends file/path text by copying it to the IDE clipboard, focusing the plugin's **Pi** tab, and pasting into the terminal.
- The clipboard is left containing the sent text.

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
