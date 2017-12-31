package de.cacodaemon.caclock.server.app.runner

import de.cacodaemon.caclock.server.app.App
import de.cacodaemon.caclock.server.app.AppPermission.*
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

    var runnableApp: RunnableApp? = null
        private set

    var runningApp: App? = null
        get() = runnableApp?.app

    @Synchronized
    fun run(app: App) {
        if (runnableApp != null) {
            runnableApp!!.stop()
        }

        runnableApp = RunnableApp(app)

        listOf(
                RunnableAppWithDateTime,
                RunnableAppWithHttpClient,
                RunnableAppWithHttpServer,
                RunnableAppWithSettings,
                RunnableAppWithProcess,
                RunnableAppWithLogger,
                RunnableAppWithLed,
                RunnableAppWithInterval
        ).forEach { f -> f.addFeature(runnableApp!!) }

        runnableApp!!.run()
    }
}