package de.cacodaemon.caclk.server

import com.google.gson.Gson
import de.cacodaemon.caclk.server.app.App
import de.cacodaemon.caclk.server.app.AppManager
import de.cacodaemon.caclk.server.app.logger.LoggerAdapter
import de.cacodaemon.caclk.server.app.logger.WebSocketAdapter
import de.cacodaemon.caclk.server.app.runner.AppRunner
import de.cacodaemon.caclk.server.app.runner.Brightness
import de.cacodaemon.caclk.server.app.runner.RunnableAppWithHttpServer
import de.cacodaemon.caclk.server.settings.Settings
import de.cacodaemon.caclk.server.settings.SettingsManager
import de.cacodaemon.rpiws28114j.*
import org.apache.log4j.BasicConfigurator
import org.apache.log4j.Level
import org.apache.log4j.Logger
import spark.Response
import spark.Spark.*
import spark.Spark.exception
import java.lang.Exception


fun main(args: Array<String>) {
    Logger.getLogger("main").info("CaClk Loaded")

    applySettings()
    AppManager.load()
    WS2811.init(WS2811Channel(
            SettingsManager.settings.gpioPin,
            SettingsManager.settings.ledCount,
            SettingsManager.settings.stripType,
            SettingsManager.settings.invert,
            SettingsManager.settings.brightness
    ))

    try {
        AppManager.getAutoStart()?.let { app -> AppRunner.run(app) }
    } catch (e: Exception) {
        Logger.getLogger("main").warn(e.message)
    }

    webSocket("/log", WebSocketAdapter::class.java)

    get("/settings") { _, response ->
        returnJson(response, SettingsManager.settings)
    }

    put("/settings", { request, response ->
        SettingsManager.settings = Gson().fromJson(request.body(), Settings::class.java)
        SettingsManager.save()
        response.status(204)
    })

    get("/app") { _, response ->
        returnJson(response, AppManager.all())
    }

    get("/app/:id") { request, response ->
        val id = request.params(":id").toString().toInt()
        returnJson(response, AppManager.get(id))
    }

    post("/app") { request, response ->
        val app = Gson().fromJson(request.body(), App::class.java)
        AppManager.add(app)
        AppManager.save()
        response.status(201)
        response.header("Location", "/app/${app.id}")
        return@post ""
    }

    put("/app/:id") { request, response ->
        val id = request.params(":id").toString().toInt()
        val app = Gson().fromJson(request.body(), App::class.java)
        AppManager.edit(id, app)
        AppManager.save()
        response.status(204)
        return@put ""
    }

    delete("/app/:id") { request, response ->
        val id = request.params(":id").toString().toInt()
        AppManager.delete(id)
        AppManager.save()
        response.status(204)
        return@delete ""
    }

    get("/app/:id/run") { request, response ->
        val id = request.params(":id").toString().toInt()
        AppRunner.run(AppManager.get(id) ?: return@get response.status(404))

        response.status(204)
    }

    get("/app/:id/api") { request, response ->
        if (AppRunner.runningApp?.id != request.params(":id").toString().toInt()) {
            response.status(403)

            return@get null
        }

        RunnableAppWithHttpServer.appApi(request, response)
    }

    get("/running-app") { _, response ->
        returnJson(response, AppRunner.runningApp)
    }
}

fun returnJson(response: Response, any: Any?): Any? {
    if (any == null) {
        response.status(404)
        return null
    }

    response.type("application/json")
    return Gson().toJson(any)
}

private fun applySettings() {
    staticFiles.location("/public")
    SettingsManager.load()
    threadPool(SettingsManager.settings.maxThreads)
    port(SettingsManager.settings.port)
    BasicConfigurator.configure()
    Logger.getRootLogger().level = Level.INFO
    Logger.getRootLogger().addAppender(LoggerAdapter)
    Brightness.value = SettingsManager.settings.softBrightness

    exception(Exception::class.java) { exception, _, response ->
        Logger.getLogger("main").warn(exception)

        response.body(Gson().toJson(mapOf(
                "name" to exception::class.java.simpleName,
                "message" to exception.message,
                "stackTrace" to exception.stackTrace
        )))
        response.status(500)
        response.type("application/json")
    }
}