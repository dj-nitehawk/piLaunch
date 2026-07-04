package dev.pilaunch

import com.intellij.openapi.Disposable
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.terminal.ui.TerminalWidget
import org.jetbrains.plugins.terminal.LocalTerminalDirectRunner
import org.jetbrains.plugins.terminal.ShellStartupOptions
import java.awt.BorderLayout
import java.beans.PropertyChangeListener
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.JPanel
import com.intellij.testFramework.LightVirtualFile

private const val PI_COMMAND = "clear && pi"
private const val PI_TAB_NAME = "Pi"

class PiSessionFile(
    val workingDirectory: String?,
) : LightVirtualFile(PI_TAB_NAME, PiSessionFileType, "") {

    init {
        isWritable = false
    }

    override fun getFileType(): FileType = PiSessionFileType
}

object PiSessionFileType : FileType {
    override fun getName(): String = "PiSession"

    override fun getDescription(): String = "piLaunch session"

    override fun getDefaultExtension(): String = ""

    override fun getIcon(): Icon = IconLoader.getIcon("/icons/pilaunch.svg", PiSessionFileType::class.java)

    override fun isBinary(): Boolean = true

    override fun isReadOnly(): Boolean = true
}

class PiSessionEditorProvider : FileEditorProvider, DumbAware {

    override fun accept(project: Project, file: VirtualFile): Boolean = file is PiSessionFile

    override fun createEditor(project: Project, file: VirtualFile): FileEditor =
        PiSessionEditor(project, file as PiSessionFile)

    override fun getEditorTypeId(): String = "pilaunch.session"

    override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.HIDE_DEFAULT_EDITOR
}

class PiSessionEditor(
    project: Project,
    private val file: PiSessionFile,
) : UserDataHolderBase(), FileEditor {

    private val sessionDisposable: Disposable = Disposer.newDisposable("piLaunch-session")
    private val view = PiSessionView(project, file, sessionDisposable)

    override fun getComponent(): JComponent = view.component

    override fun getPreferredFocusedComponent(): JComponent? = view.preferredFocusComponent()

    override fun getName(): String = PI_TAB_NAME

    override fun setState(state: FileEditorState) {}

    override fun isModified(): Boolean = false

    override fun isValid(): Boolean = true

    override fun addPropertyChangeListener(listener: PropertyChangeListener) {}

    override fun removePropertyChangeListener(listener: PropertyChangeListener) {}

    override fun getFile(): VirtualFile = file

    override fun dispose() {
        Disposer.dispose(sessionDisposable)
    }
}

private class PiSessionView(
    project: Project,
    file: PiSessionFile,
    parent: Disposable,
) {
    private val widget: TerminalWidget = PiLauncher.launch(project, parent, file.workingDirectory)

    val component: JComponent = JPanel(BorderLayout()).apply {
        add(widget.component, BorderLayout.CENTER)
    }

    fun preferredFocusComponent(): JComponent? = widget.preferredFocusableComponent
}

private object PiLauncher {

    fun launch(project: Project, parent: Disposable, workingDirectory: String?): TerminalWidget {
        val options = ShellStartupOptions.Builder()
            .workingDirectory(workingDirectory)
            .build()
        val widget = PiTerminalRunner(project).startShellTerminalWidget(parent, options, true)
        widget.sendCommandToExecute(PI_COMMAND)
        return widget
    }
}

private class PiTerminalRunner(project: Project) : LocalTerminalDirectRunner(project)
