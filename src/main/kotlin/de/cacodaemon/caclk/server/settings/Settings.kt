package de.cacodaemon.caclk.server.settings

import de.cacodaemon.rpiws28114j.StripType

data class Settings (
        val port: Int,
        val maxThreads: Int,
        val gpioPin: Int,
        val ledCount: Int,
        val stripType: StripType,
        val invert: Boolean,
        val brightness: Int
)