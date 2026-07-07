package dev.pilaunch

import com.intellij.openapi.Disposable
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.terminal.JBTerminalWidget
import com.jediterm.terminal.ui.TerminalPanel
import java.awt.Cursor
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.lang.reflect.Method
import java.nio.file.InvalidPathException
import java.nio.file.Path
import javax.swing.SwingUtilities
import kotlin.io.path.isRegularFile

private val FILE_REFERENCE_PATTERN = Regex(
    "(?<![\\w./-])(?<path>(?:\\./)?(?:[A-Za-z0-9_.-]+/)+[A-Za-z0-9_.-]+\\.[A-Za-z0-9][A-Za-z0-9_-]*)(?::(?<line>\\d+)(?:-\\d+)?)?"
)

internal class PiTerminalFileLinkHandler(
    private val project: Project,
    workingDirectory: String?,
    private val terminalPanel: TerminalPanel,
) : MouseAdapter() {
    private val basePath: Path? = workingDirectory?.toPathOrNull()
    private val pointConverter: Method? = panelPointConverter()
    private val fileCache = mutableMapOf<String, VirtualFile>()
    private var previousCursor: Cursor? = null
    private var hoveredLink: PiTerminalFileLink? = null

    override fun mouseMoved(event: MouseEvent) {
        setHoveredLink(linkAt(event.point))
    }

    override fun mouseExited(event: MouseEvent) {
        setHoveredLink(null)
    }

    override fun mouseClicked(event: MouseEvent) {
        if (!SwingUtilities.isLeftMouseButton(event) || event.clickCount != 1) return

        val link = hoveredLink ?: linkAt(event.point) ?: return
        link.navigate(project)
        event.consume()
    }

    fun dispose() {
        setHoveredLink(null)
        terminalPanel.removeMouseListener(this)
        terminalPanel.removeMouseMotionListener(this)
    }

    private fun setHoveredLink(link: PiTerminalFileLink?) {
        if (hoveredLink == link) return

        hoveredLink = link
        if (link == null) {
            previousCursor?.let { terminalPanel.cursor = it }
            previousCursor = null
            terminalPanel.toolTipText = null
        } else {
            if (previousCursor == null) previousCursor = terminalPanel.cursor
            terminalPanel.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            terminalPanel.toolTipText = link.displayText
        }
    }

    private fun linkAt(point: Point): PiTerminalFileLink? {
        val cell = terminalCell(point) ?: return null
        val line = lineText(cell.line) ?: return null
        if (cell.column >= line.length) return null

        return FILE_REFERENCE_PATTERN.findAll(line).firstNotNullOfOrNull { match ->
            if (cell.column !in match.range) null else linkFromMatch(match)
        }
    }

    private fun terminalCell(point: Point): TerminalCell? {
        val converter = pointConverter ?: return null
        val converted = runCatching {
            converter.invoke(terminalPanel, point) as? com.jediterm.core.compatibility.Point
        }.getOrNull() ?: return null

        return TerminalCell(converted.x, converted.y)
    }

    private fun lineText(lineIndex: Int): String? {
        val buffer = terminalPanel.terminalTextBuffer
        buffer.lock()
        return try {
            buffer.getLine(lineIndex).text
        } catch (_: RuntimeException) {
            null
        } finally {
            buffer.unlock()
        }
    }

    private fun linkFromMatch(match: MatchResult): PiTerminalFileLink? {
        val rawPath = match.groups["path"]?.value ?: return null
        val file = resolveFile(rawPath) ?: return null
        val lineNumber = match.groups["line"]?.value?.toIntOrNull()?.takeIf { it > 0 }

        return PiTerminalFileLink(file, lineNumber, match.value)
    }

    private fun resolveFile(rawPath: String): VirtualFile? {
        fileCache[rawPath]?.takeIf { it.isValid }?.let { return it }
        fileCache.remove(rawPath)

        return resolveFileUncached(rawPath)?.also { fileCache[rawPath] = it }
    }

    private fun resolveFileUncached(rawPath: String): VirtualFile? {
        val base = basePath ?: return null
        val candidate = rawPath.removePrefix("./").toPathOrNull() ?: return null
        val resolved = if (candidate.isAbsolute) candidate.normalize() else base.resolve(candidate).normalize()

        if (!resolved.startsWith(base) || !resolved.isRegularFile()) return null

        return LocalFileSystem.getInstance().refreshAndFindFileByIoFile(resolved.toFile())
    }

    private fun PiTerminalFileLink.navigate(targetProject: Project) {
        if (lineNumber == null) {
            FileEditorManager.getInstance(targetProject).openFile(file, true)
        } else {
            OpenFileDescriptor(targetProject, file, lineNumber - 1, 0).navigate(true)
        }
    }

    private data class TerminalCell(
        val column: Int,
        val line: Int,
    )

    private data class PiTerminalFileLink(
        val file: VirtualFile,
        val lineNumber: Int?,
        val displayText: String,
    )

    companion object {
        fun install(project: Project, workingDirectory: String?, widget: JBTerminalWidget, parent: Disposable) {
            val handler = PiTerminalFileLinkHandler(project, workingDirectory, widget.terminalPanel)
            widget.terminalPanel.addMouseListener(handler)
            widget.terminalPanel.addMouseMotionListener(handler)
            Disposer.register(parent) { handler.dispose() }
        }

        private fun panelPointConverter(): Method? = runCatching {
            TerminalPanel::class.java.getDeclaredMethod("panelToCharCoords", Point::class.java).apply {
                isAccessible = true
            }
        }.getOrNull()
    }
}

private fun String.toPathOrNull(): Path? =
    try {
        Path.of(this)
    } catch (_: InvalidPathException) {
        null
    }
