package de.cacodaemon.caclk.server.app.runner

import de.cacodaemon.caclk.server.app.AppPermission
import org.apache.log4j.Logger

object RunnableAppWithDateTime : RunnableAppFeature {

    private val logger = Logger.getRootLogger()

    override fun addFeature(runnableApp: RunnableApp): RunnableApp {
        if (!runnableApp.app.permissions.contains(AppPermission.DATE_TIME)) {
            return runnableApp
        }

        allowClass(runnableApp, "DateTime", "java.time.LocalDateTime")
        allowClass(runnableApp, "ChronoUnit", "java.time.temporal.ChronoUnit")

        logger.debug("Added \"DateTime\" to app \"${runnableApp.app.name}:${runnableApp.app.version}\"")

        return runnableApp
    }

    private fun allowClass(runnableApp: RunnableApp, nameInScript: String, className: String) {
        runnableApp.addAllowableClass(className)
        runnableApp.addAppScriptHeader("var $nameInScript = Java.type('$className')")
    }
}