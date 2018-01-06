package de.cacodaemon.caclk.server.app.runner

interface RunnableAppFeature {

    fun addFeature(runnableApp: RunnableApp): RunnableApp
}