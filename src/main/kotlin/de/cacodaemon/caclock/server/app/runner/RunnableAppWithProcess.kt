package de.cacodaemon.caclock.server.app.runner

import de.cacodaemon.caclock.server.app.App
import de.cacodaemon.caclock.server.app.AppPermission
import org.apache.log4j.Logger

object RunnableAppWithProcess {

    private val logger = Logger.getRootLogger()

    fun decorate(runnableApp: RunnableApp): RunnableApp {
        if (!runnableApp.app.permissions.contains(AppPermission.PROCESS)) {
            return runnableApp
        }

        allowClass(runnableApp, "Process", "de.cacodaemon.caclock.server.app.runner.Process")

        logger.debug("Added \"Process\" to app \"${runnableApp.app.name}:${runnableApp.app.version}\"")

        return runnableApp
    }

    private fun allowClass(runnableApp: RunnableApp, nameInScript: String, className: String) {
        runnableApp.addAllowableClass(className)
        runnableApp.addAppScriptHeader("var $nameInScript = Java.type('$className');")
    }
}