package de.cacodaemon.caclk.server.app.logger

import com.google.gson.Gson
import org.apache.log4j.AppenderSkeleton
import org.apache.log4j.spi.LoggingEvent
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage
import org.eclipse.jetty.websocket.api.annotations.WebSocket
import org.eclipse.jetty.websocket.api.*

object WebSocketSessions {

    private val sessions: MutableList<Session> = ArrayList<Session>()

    fun remove(session: Session) = sessions.remove(session)

    fun add(session: Session) = sessions.add(session)

    fun broadcast(message: Any) {
        sessions.filter { session -> session.isOpen }
                .forEach { session ->
                    session.remote.sendString(Gson().toJson(message))
                }
    }
}