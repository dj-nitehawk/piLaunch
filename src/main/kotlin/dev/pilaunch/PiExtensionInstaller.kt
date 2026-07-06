package dev.pilaunch

import com.intellij.openapi.application.PathManager
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

object PiExtensionInstaller {

    fun attentionExtensionPath(): Path {
        val targetDir = Path.of(PathManager.getSystemPath(), "piLaunch")
        Files.createDirectories(targetDir)

        val target = targetDir.resolve("pilaunch-attention.ts")
        val resource = javaClass.classLoader.getResourceAsStream("pi/pilaunch-attention.ts")
            ?: error("Missing bundled resource: pi/pilaunch-attention.ts")

        resource.use { input ->
            Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING)
        }

        return target
    }
}
