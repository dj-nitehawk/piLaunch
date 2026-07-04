import org.jetbrains.intellij.platform.gradle.extensions.intellijPlatform

rootProject.name = "piLaunch"

plugins {
    id("org.jetbrains.intellij.platform.settings") version "2.16.0"
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        intellijPlatform {
            defaultRepositories()
        }
    }
}
