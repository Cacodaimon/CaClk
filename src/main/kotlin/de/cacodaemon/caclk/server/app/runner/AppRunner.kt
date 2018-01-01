package de.cacodaemon.caclk.server.app.runner

import de.cacodaemon.caclk.server.app.App
import de.cacodaemon.caclk.server.app.AppPermission.*
import jdk.nashorn.api.scripting.ClassFilter
import jdk.nashorn.api.scripting.NashornScriptEngineFactory
import okhttp3.OkHttpClient
import org.apache.log4j.Logger
import spark.Request
import spark.Response
import java.util.*
import java.util.Optional.ofNullable
import javax.script.Invocable
import javax.script.ScriptEngine

object AppRunner {

    var runnableApp: de.cacodaemon.caclk.server.app.runner.RunnableApp? = null
        private set

    var runningApp: de.cacodaemon.caclk.server.app.App? = null
        get() = runnableApp?.app

    @Synchronized
    fun run(app: de.cacodaemon.caclk.server.app.App) {
        if (runnableApp != null) {
            runnableApp!!.stop()
        }

        runnableApp = de.cacodaemon.caclk.server.app.runner.RunnableApp(app)

        listOf(
                de.cacodaemon.caclk.server.app.runner.RunnableAppWithDateTime,
                de.cacodaemon.caclk.server.app.runner.RunnableAppWithHttpClient,
                de.cacodaemon.caclk.server.app.runner.RunnableAppWithHttpServer,
                de.cacodaemon.caclk.server.app.runner.RunnableAppWithSettings,
                de.cacodaemon.caclk.server.app.runner.RunnableAppWithProcess,
                de.cacodaemon.caclk.server.app.runner.RunnableAppWithLogger,
                de.cacodaemon.caclk.server.app.runner.RunnableAppWithLed,
                de.cacodaemon.caclk.server.app.runner.RunnableAppWithInterval
        ).forEach { f -> f.addFeature(runnableApp!!) }

        runnableApp!!.run()
    }
}