package de.cacodaemon.caclk.server.app.runner

import de.cacodaemon.caclk.server.app.AppPermission
import org.apache.log4j.Logger
import javax.script.Invocable

object RunnableAppWithLogger : RunnableAppFeature {

    private val logger = Logger.getRootLogger()

    override fun addFeature(runnableApp: RunnableApp): RunnableApp {
        if (!runnableApp.app.permissions.contains(AppPermission.LOGGER)) {
            return runnableApp
        }

        runnableApp.addAppScriptHeader("var log = null")
        runnableApp.addAppScriptHeader("var addLogger = function(logger) { log = function (message) { logger.info(message) } }")

        runnableApp.addStartupCallbacks({
            (runnableApp.getEngine() as Invocable).invokeFunction("addLogger", Logger.getLogger("${runnableApp.app.name}:${runnableApp.app.version}"))
        })

        logger.debug("Added \"Logger\" to app \"${runnableApp.app.name}:${runnableApp.app.version}\"")

        return runnableApp
    }

    private fun allowClass(runnableApp: RunnableApp, nameInScript: String, className: String) {
        runnableApp.addAllowableClass(className)
        runnableApp.addAppScriptHeader("var $nameInScript = Java.type('$className')")
    }
}