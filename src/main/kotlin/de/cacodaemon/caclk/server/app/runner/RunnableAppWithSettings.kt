package de.cacodaemon.caclk.server.app.runner

import de.cacodaemon.caclk.server.app.AppPermission
import org.apache.log4j.Logger

object RunnableAppWithSettings : RunnableAppFeature {

    private val logger = Logger.getRootLogger()

    override fun addFeature(runnableApp: RunnableApp): RunnableApp {
        if (!runnableApp.app.permissions.contains(AppPermission.SETTINGS)) {
            return runnableApp
        }

        runnableApp.addAppScriptHeader("var settings = ${runnableApp.app.settings}")

        logger.debug("Added \"Settings\" to app \"${runnableApp.app.name}:${runnableApp.app.version}\"")

        return runnableApp
    }
}