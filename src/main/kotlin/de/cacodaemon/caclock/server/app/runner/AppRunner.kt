package de.cacodaemon.caclock.server.app.runner

import de.cacodaemon.caclock.server.app.App
import de.cacodaemon.caclock.server.app.AppPermission.*
import jdk.nashorn.api.scripting.ClassFilter
import jdk.nashorn.api.scripting.NashornScriptEngineFactory
import org.apache.log4j.Logger
import spark.Request
import spark.Response
import java.util.*
import javax.script.Invocable
import javax.script.ScriptEngine

object AppRunner {

    private val allowedClasses = listOf(
            "de.cacodaemon.rpiws28114j.Color",
            "de.cacodaemon.rpiws28114j.WS2811",
            "java.lang.Integer",
            "de.cacodaemon.caclock.server.fonts.Fonts",
            "java.time.LocalDateTime", //TODO Permissions…
            "de.cacodaemon.caclock.server.app.runner.Process" //TODO Permissions…
    )

    private val typeDefs = """
var Color = Java.type('de.cacodaemon.rpiws28114j.Color');
var Integer = Java.type('java.lang.Integer');
var LocalDateTime = Java.type('java.time.LocalDateTime'); //TODO Permissions…
var Fonts = Java.type('de.cacodaemon.caclock.server.fonts.Fonts');
var Process = Java.type('de.cacodaemon.caclock.server.app.runner.Process');
"""

    private var timer = Timer("AppRunner", true)

    var runningApp: App? = null
        private set

    var engine: ScriptEngine? = null

    @Synchronized
    fun run(app: App) {
        runningApp = app
        timer.cancel()
        timer.purge()
        timer = Timer("AppRunner", true)

        engine = NashornScriptEngineFactory()
                .getScriptEngine(ClassFilter { className ->
                    if (allowedClasses.contains(className)) {
                        return@ClassFilter true
                    }

                    if (app.permissions.contains(HTTP_CLIENT)) {
                        return@ClassFilter className == "httpClassName"
                    }

                    if (app.permissions.contains(LED)) {
                        return@ClassFilter className == "de.cacodaemon.rpiws28114j.WS2811"
                    }

                    if (app.permissions.contains(DATE_TIME)) {
                        return@ClassFilter className == "java.time.LocalDateTime"
                    }

                    false
                })

        Logger.getRootLogger().info("Running app \"${app.name}:${app.version}\".")

        engine?.eval("$typeDefs\n${app.code}")
        (engine as Invocable).invokeFunction("app", LedDisplay, app.settings ?: app.defaultSettings)

        if (app.interval != null && app.permissions.contains(INTERVAL)) {
            Logger.getRootLogger().info("Adding timer to app \"${app.name}:${app.version}\" with interval of ${app.interval}.")
            timer.schedule(object : TimerTask() {
                override fun run() {
                    (engine as Invocable).invokeFunction("interval", System.currentTimeMillis())
                }
            }, 0, app.interval)
        }
    }

    fun appApi(request: Request, response: Response): Any? {
        if (runningApp == null) {
            response.status(404)

            return null
        }

        if (!runningApp!!.permissions.contains(HTTP_SERVER)) {
            response.status(405)

            return null
        }

        return (engine as Invocable).invokeFunction("api", request, response)
    }
}