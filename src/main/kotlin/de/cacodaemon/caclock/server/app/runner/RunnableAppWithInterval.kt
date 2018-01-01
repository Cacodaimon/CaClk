package de.cacodaemon.caclock.server.app.runner

import de.cacodaemon.caclock.server.app.AppPermission
import de.cacodaemon.caclock.server.fonts.Fonts
import org.apache.log4j.Logger
import java.time.LocalDateTime
import java.util.*
import javax.script.Invocable

object RunnableAppWithInterval : RunnableAppFeature {

    private val logger = Logger.getRootLogger()

    override fun addFeature(runnableApp: RunnableApp): RunnableApp {
        if (!runnableApp.app.permissions.contains(AppPermission.INTERVAL)) {
            return runnableApp
        }

        if (runnableApp.app.interval == null) {
            logger.warn("Adding timer to app \"${runnableApp.app.name}:${runnableApp.app.version}\" failed, interval in ms is not set.")

            return runnableApp
        }
        val interval: Long = runnableApp.app.interval
        val timer = Timer("Timer:${runnableApp.app.name}:${runnableApp.app.version}", true)

        timer.schedule(object : TimerTask() {
            override fun run() {
                try {
                    (runnableApp.getEngine() as Invocable).invokeFunction("interval", System.currentTimeMillis())
                } catch (e: Exception) {
                    logger.warn(e.message)
                }
            }
        }, 500, interval)

        runnableApp.addTearDownCallbacks({
            timer.cancel()
            timer.purge()
        })

        logger.debug("Added \"interval\" to app \"${runnableApp.app.name}:${runnableApp.app.version}\"")

        return runnableApp
    }
}