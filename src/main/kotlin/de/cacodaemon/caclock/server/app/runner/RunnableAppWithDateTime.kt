package de.cacodaemon.caclock.server.app.runner

import de.cacodaemon.caclock.server.app.App
import de.cacodaemon.caclock.server.app.AppPermission
import org.apache.log4j.Logger

object RunnableAppWithDateTime {

    private val logger = Logger.getRootLogger()

    fun decorate(runnableApp: RunnableApp): RunnableApp {
        if (!runnableApp.app.permissions.contains(AppPermission.DATE_TIME)) {
            return runnableApp
        }

        allowClass(runnableApp, "DateTime", "java.time.LocalDateTime")

        logger.debug("Added \"DateTime\" to app \"${runnableApp.app.name}:${runnableApp.app.version}\"")

        return runnableApp
    }

    private fun allowClass(runnableApp: RunnableApp, nameInScript: String, className: String) {
        runnableApp.addAllowableClass(className)
        runnableApp.addAppScriptHeader("var $nameInScript = Java.type('$className');")
    }
}