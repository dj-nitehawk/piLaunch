package dev.pilaunch

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManagerKeys
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.fileEditor.ex.FileEditorOpenRequest
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class OpenPiOnStartupActivity : StartupActivity.DumbAware {

    override fun runActivity(project: Project) {
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
                    selectAsCurrent = false,
                    reuseOpen = true,
                    requestFocus = false,
                    pin = true,
                ),
            )
            fileEditorManager.windows
                .firstOrNull { it.isFileOpen(file) }
                ?.setFilePinned(file, true)
        }
    }
}
