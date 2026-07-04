# piLaunch

JetBrains IDE plugin that starts `pi` in a pinned editor tab when a project opens.

## Requirements

- IntelliJ IDEA 2024.3 or newer
- Bundled **Terminal** plugin enabled
- `pi` available on `PATH`

## Building from source

```bash
./gradlew runIde        # launch a sandbox IDE with the plugin
./gradlew buildPlugin   # produce build/distributions/*.zip
```

Building requires JDK 21.
