package de.cacodaemon.caclock.server.app.runner

interface RunnableAppFeature {

    fun addFeature(runnableApp: RunnableApp): RunnableApp
}