package de.cacodaemon.caclock.server.app.runner

import de.cacodaemon.caclock.server.app.AppPermission
import org.apache.log4j.Logger
import spark.Request
import spark.Response
import javax.script.Invocable

object RunnableAppWithHttpServer : RunnableAppFeature {

    private val logger = Logger.getRootLogger()

    private var runningApp: RunnableApp? = null

    override fun addFeature(runnableApp: RunnableApp): RunnableApp {
        if (!runnableApp.app.permissions.contains(AppPermission.HTTP_SERVER)) {
            runningApp = null

            return runnableApp
        }

        runningApp = runnableApp
        runnableApp.allowPublicFunction("api")
        logger.debug("Allowed \"http api\" to app \"${runnableApp.app.name}:${runnableApp.app.version}\"")

        return runnableApp
    }

    public fun appApi(request: Request, response: Response): Any? {
        if (runningApp == null) {
            response.status(404)

            return null
        }

        return (runningApp!!.getEngine() as Invocable).invokeFunction("api", request, response)
    }
}