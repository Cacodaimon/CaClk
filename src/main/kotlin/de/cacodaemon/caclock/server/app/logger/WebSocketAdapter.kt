package de.cacodaemon.caclock.server.app.logger

import com.google.gson.Gson
import org.apache.log4j.AppenderSkeleton
import org.apache.log4j.spi.LoggingEvent
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage
import org.eclipse.jetty.websocket.api.annotations.WebSocket
import org.eclipse.jetty.websocket.api.*

@WebSocket
class WebSocketAdapter {

    @OnWebSocketConnect
    fun connected(session: Session) {
        WebSocketSessions.add(session)
    }

    @OnWebSocketMessage
    fun message(session: Session, message: String) {
        session.remote.sendString(Gson().toJson("You will receive the server log messages here, sending messages is not supported."))
    }

    @OnWebSocketClose
    fun disconnect(session: Session, code: Int, reason: String?) {
        WebSocketSessions.remove(session)
    }
}