package de.cacodaemon.caclock.server.app.runner

import de.cacodaemon.caclock.server.app.App
import de.cacodaemon.caclock.server.app.AppPermission
import org.apache.log4j.Logger
import java.util.*
import javax.script.Invocable

object RunnableAppWithInterval {

    private val logger = Logger.getRootLogger()

    fun decorate(runnableApp: RunnableApp): RunnableApp {
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
                (runnableApp.getEngine() as Invocable).invokeFunction("interval", System.currentTimeMillis())
            }
        }, 0, interval)

        runnableApp.addTearDownCallbacks({
            timer.cancel()
            timer.purge()
        })

        logger.debug("Added \"interval\" to app \"${runnableApp.app.name}:${runnableApp.app.version}\"")

        return runnableApp
    }
}