---
type: Reference
title: Dependencies
description: Runtime versions, package management, key libraries, and compatibility constraints.
tags: [dependencies, gradle, intellij, kotlin]
---

# Dependencies

## Runtime and build versions

| Dependency | Version/source |
| --- | --- |
| JDK / Java target | 21 (`build.gradle.kts`) |
| Kotlin JVM plugin | `2.4.0` (`build.gradle.kts`) |
| Gradle wrapper | `9.6.0` (`gradle/wrapper/gradle-wrapper.properties`) |
| IntelliJ Platform Gradle settings plugin | `2.16.0` (`settings.gradle.kts`) |
| IntelliJ Platform Gradle plugin | `org.jetbrains.intellij.platform` (`build.gradle.kts`, version provided by settings plugin resolution) |
| Changelog plugin | `2.2.1` (`build.gradle.kts`) |
| Target IDE | Rider `2026.1.3` (`gradle.properties`) |
| Plugin since-build | `261` (`gradle.properties`) |

## Key platform dependencies

- `intellijPlatform { rider(platformVersion) { useInstaller = false } }`
- Bundled Terminal plugin: `org.jetbrains.plugins.terminal`
- Plugin descriptor also declares dependency on `com.intellij.modules.platform` and `org.jetbrains.plugins.terminal`.

## Runtime external dependency

- `pi` CLI must be available on `PATH` when the plugin launches the terminal command.
- The bundled Pi extension imports `ExtensionAPI` from `@earendil-works/pi-coding-agent` at Pi extension runtime; no Node/package manifest is present in this repository.

## Package management

- Gradle wrapper is the canonical build entry point.
- Repositories are Maven Central plus IntelliJ Platform default repositories.
- Kotlin standard library default dependency is disabled via `kotlin.stdlib.default.dependency = false`.

## Update guidance

- Keep `pluginSinceBuild`, `platformVersion`, Gradle IntelliJ Platform config, and README requirements aligned.
- If updating Terminal API usage, verify compatibility with the target IntelliJ Platform version.
- If adding dependency versions or test frameworks, document them here and in workflows/testing when they affect commands.
- Preserve JDK 21 target unless the platform target changes require otherwise.

## Sources

- `build.gradle.kts`
- `settings.gradle.kts`
- `gradle.properties`
- `gradle/wrapper/gradle-wrapper.properties`
- `README.md`
- `src/main/resources/META-INF/plugin.xml`
- `src/main/resources/pi/pilaunch-attention.ts`
