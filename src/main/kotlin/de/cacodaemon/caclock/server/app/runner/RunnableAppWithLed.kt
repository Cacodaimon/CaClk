package de.cacodaemon.caclock.server.app.runner

import de.cacodaemon.caclock.server.app.App
import de.cacodaemon.caclock.server.app.AppPermission
import org.apache.log4j.Logger

object RunnableAppWithLed {

    private val logger = Logger.getRootLogger()

    fun decorate(runnableApp: RunnableApp): RunnableApp {
        if (!runnableApp.app.permissions.contains(AppPermission.LED)) {
            return runnableApp
        }

        allowClass(runnableApp, "LedDisplay", "de.cacodaemon.caclock.server.app.runner.LedDisplay")
        allowClass(runnableApp, "Color", "de.cacodaemon.rpiws28114j.Color")
        allowClass(runnableApp, "Fonts", "de.cacodaemon.caclock.server.fonts.Fonts")

        logger.debug("Added \"LedDisplay\" to app \"${runnableApp.app.name}:${runnableApp.app.version}\"")

        return runnableApp
    }

    private fun allowClass(runnableApp: RunnableApp, nameInScript: String, className: String) {
        runnableApp.addAllowableClass(className)
        runnableApp.addAppScriptHeader("var $nameInScript = Java.type('$className');")
    }
}