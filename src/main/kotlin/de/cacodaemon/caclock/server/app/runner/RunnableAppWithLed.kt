package de.cacodaemon.caclock.server.app.runner

import de.cacodaemon.caclock.server.app.AppPermission
import org.apache.log4j.Logger
import javax.script.Invocable

object RunnableAppWithLed  : RunnableAppFeature {

    private val logger = Logger.getRootLogger()

    override fun addFeature(runnableApp: RunnableApp): RunnableApp {
        if (!runnableApp.app.permissions.contains(AppPermission.LED)) {
            return runnableApp
        }

        allowClass(runnableApp, "Color", "de.cacodaemon.rpiws28114j.Color")
        allowClass(runnableApp, "Fonts", "de.cacodaemon.caclock.server.fonts.Fonts")

        runnableApp.addAppScriptHeader("var ledDisplay = null")
        runnableApp.addAppScriptHeader("var addLedDisplay = function(ledDisplayToAdd) { ledDisplay = ledDisplayToAdd }")

        runnableApp.addStartupCallbacks({
            (runnableApp.getEngine() as Invocable).invokeFunction("addLedDisplay", LedDisplay)
        })

        logger.debug("Added \"LedDisplay\" to app \"${runnableApp.app.name}:${runnableApp.app.version}\"")

        return runnableApp
    }

    private fun allowClass(runnableApp: RunnableApp, nameInScript: String, className: String) {
        runnableApp.addAllowableClass(className)
        runnableApp.addAppScriptHeader("var $nameInScript = Java.type('$className')")
    }
}