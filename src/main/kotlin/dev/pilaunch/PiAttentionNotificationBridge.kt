package dev.pilaunch

import com.intellij.notification.Notification
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import java.net.InetAddress
import java.net.InetSocketAddress
import java.security.SecureRandom
import java.util.Base64
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Service(Service.Level.PROJECT)
class PiAttentionNotificationBridge(private val project: Project) : Disposable {

    private var server: HttpServer? = null
    private var executor: ExecutorService? = null
    private var token: String? = null
    private var lastNotificationAt = 0L
    private val activeNotifications = mutableListOf<Notification>()

    init {
        project.messageBus.connect(this).subscribe(
            FileEditorManagerListener.FILE_EDITOR_MANAGER,
            object : FileEditorManagerListener {
                override fun selectionChanged(event: FileEditorManagerEvent) {
                    if (event.newFile is PiSessionFile) {
                        expireAttentionNotifications()
                    }
                }
            },
        )
    }

    fun start(sessionDisposable: Disposable): Endpoint {
        val existingServer = server
        val existingToken = token
        if (existingServer != null && existingToken != null) {
            return Endpoint(
                url = "http://127.0.0.1:${existingServer.address.port}/notify",
                token = existingToken,
            )
        }

        val newToken = createToken()
        val bindAddress = InetSocketAddress(InetAddress.getLoopbackAddress(), 0)
        val newServer = HttpServer.create(bindAddress, 0)
        val newExecutor = Executors.newSingleThreadExecutor { runnable ->
            Thread(runnable, "piLaunch-attention-bridge").apply {
                isDaemon = true
            }
        }

        newServer.createContext("/notify") { exchange ->
            handleNotify(exchange, newToken)
        }
        newServer.executor = newExecutor
        newServer.start()

        server = newServer
        executor = newExecutor
        token = newToken

        Disposer.register(sessionDisposable) {
            stop()
        }

        return Endpoint(
            url = "http://127.0.0.1:${newServer.address.port}/notify",
            token = newToken,
        )
    }

    private fun handleNotify(exchange: HttpExchange, expectedToken: String) {
        try {
            if (exchange.requestMethod != "POST") {
                exchange.sendStatus(405)
                return
            }

            val actualToken = exchange.requestHeaders.getFirst("x-pilaunch-token")
            if (actualToken != expectedToken) {
                exchange.sendStatus(401)
                return
            }

            exchange.requestBody.use { it.readBytes() }
            exchange.sendStatus(204)

            showAttentionNotification()
        } catch (_: Exception) {
            if (exchange.responseCode == -1) {
                exchange.sendStatus(500)
            }
        } finally {
            exchange.close()
        }
    }

    private fun showAttentionNotification() {
        ApplicationManager.getApplication().invokeLater {
            if (project.isDisposed || isPiSessionSelected()) return@invokeLater

            val now = System.currentTimeMillis()
            if (now - lastNotificationAt < NOTIFICATION_DEBOUNCE_MILLIS) return@invokeLater
            lastNotificationAt = now

            val notification = NotificationGroupManager.getInstance()
                .getNotificationGroup(NOTIFICATION_GROUP_ID)
                .createNotification("Pi needs attention", NotificationType.INFORMATION)

            activeNotifications += notification
            notification.notify(project)
        }
    }

    private fun expireAttentionNotifications() {
        val notifications = activeNotifications.toList()
        activeNotifications.clear()
        notifications.forEach { it.expire() }
    }

    private fun isPiSessionSelected(): Boolean =
        FileEditorManager.getInstance(project).selectedFiles.any { it is PiSessionFile }

    private fun stop() {
        server?.stop(0)
        server = null
        executor?.shutdownNow()
        executor = null
        token = null
    }

    override fun dispose() {
        expireAttentionNotifications()
        stop()
    }

    data class Endpoint(
        val url: String,
        val token: String,
    )

    companion object {
        private const val NOTIFICATION_GROUP_ID = "piLaunch"
        private const val NOTIFICATION_DEBOUNCE_MILLIS = 1_000L

        fun getInstance(project: Project): PiAttentionNotificationBridge = project.service()

        private fun createToken(): String {
            val bytes = ByteArray(32)
            SecureRandom().nextBytes(bytes)
            return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
        }
    }
}

private fun HttpExchange.sendStatus(statusCode: Int) {
    sendResponseHeaders(statusCode, -1)
}
