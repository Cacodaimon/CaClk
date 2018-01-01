package de.cacodaemon.caclk.server.app

data class App (
        var id: Int,
        val name: String,
        val version: String,
        val author: de.cacodaemon.caclk.server.app.Author,
        val code: String,
        val permissions: List<de.cacodaemon.caclk.server.app.AppPermission>,
        val autoStart: Boolean,
        val interval: Long?,
        val description: String?,
        val settings: Any?,
        val defaultSettings: Any?
)