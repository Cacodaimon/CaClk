package de.cacodaemon.caclk.server.app.logger

import com.google.gson.Gson
import org.apache.log4j.AppenderSkeleton
import org.apache.log4j.spi.LoggingEvent
import org.eclipse.jetty.websocket.api.annotations.WebSocket

object LoggerAdapter : AppenderSkeleton() {

    data class SerializableLoggingEvent(
            val loggerName: String?,
            val renderedMessage: String?,
            val threadName: String?,
            val timeStamp: Long?,
            val level: String?
    )

    override fun requiresLayout(): Boolean {
        return false
    }

    override fun append(event: LoggingEvent?) {
        WebSocketSessions.broadcast(SerializableLoggingEvent(
                loggerName = event?.loggerName,
                renderedMessage = event?.renderedMessage,
                threadName = event?.threadName,
                timeStamp = event?.timeStamp,
                level = event?.getLevel()?.toString()
        ))
    }

    override fun close() {
    }
}