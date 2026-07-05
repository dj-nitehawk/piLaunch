package dev.pilaunch

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManagerKeys
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.fileEditor.ex.FileEditorOpenRequest
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VirtualFile

abstract class SendToPiAction : AnAction(), DumbAware {

    final override fun update(event: AnActionEvent) {
        event.presentation.isEnabled = sourceContext(event) != null
    }

    final override fun actionPerformed(event: AnActionEvent) {
        val context = sourceContext(event) ?: return
        sendToPiPrompt(context.project, textFor(context))
    }

    protected abstract fun textFor(context: SourceContext): String

    protected data class SourceContext(
        val project: Project,
        val editor: com.intellij.openapi.editor.Editor,
        val file: VirtualFile,
    )

    private fun sourceContext(event: AnActionEvent): SourceContext? {
        val project = event.project ?: return null
        val editor = event.getData(CommonDataKeys.EDITOR) ?: return null
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE) ?: return null
        if (file.isDirectory || file is PiSessionFile) return null
        return SourceContext(project, editor, file)
    }
}

class SendCurrentFilePathToPiAction : SendToPiAction() {

    override fun textFor(context: SourceContext): String = context.file.displayPath(context.project)
}

class SendCurrentFileLocationToPiAction : SendToPiAction() {

    override fun textFor(context: SourceContext): String {
        val path = context.file.displayPath(context.project)
        val selection = context.editor.selectionModel
        val start = selection.selectionStartPosition
        val end = selection.selectionEndPosition

        if (selection.hasSelection() && start != null && end != null && start.line != end.line) {
            return "$path:${start.line + 1}-${end.line + 1}"
        }

        val selectedText = selection.selectedText
        if (selectedText != null && selectedText.isSingleSymbol()) {
            val line = if (start != null) start.line + 1 else context.editor.caretModel.logicalPosition.line + 1
            return "$selectedText ($path:$line)"
        }

        return "$path:${context.editor.caretModel.logicalPosition.line + 1}"
    }
}

private fun VirtualFile.displayPath(project: Project): String {
    val basePath = project.basePath ?: return path
    return FileUtil.getRelativePath(basePath, path, '/') ?: path
}

private fun String.isSingleSymbol(): Boolean = isNotBlank() && none { it.isWhitespace() }

private fun sendToPiPrompt(project: Project, text: String) {
    ApplicationManager.getApplication().invokeLater {
        if (project.isDisposed) return@invokeLater

        val fileEditorManager = FileEditorManagerEx.getInstanceEx(project)
        val file = fileEditorManager.openFiles
            .filterIsInstance<PiSessionFile>()
            .firstOrNull()
            ?: PiSessionFile(project.basePath)

        file.putUserData(FileEditorManagerKeys.FORBID_PREVIEW_TAB, true)
        fileEditorManager.openFile(
            file,
            FileEditorOpenRequest(
                selectAsCurrent = true,
                reuseOpen = true,
                requestFocus = true,
                pin = true,
            ),
        )
        fileEditorManager.windows
            .firstOrNull { it.isFileOpen(file) }
            ?.setFilePinned(file, true)

        fileEditorManager.getEditors(file)
            .filterIsInstance<PiSessionEditor>()
            .firstOrNull()
            ?.pasteTextToPrompt(text)
    }
}
