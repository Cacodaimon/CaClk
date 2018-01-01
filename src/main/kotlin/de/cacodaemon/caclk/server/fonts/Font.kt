package de.cacodaemon.caclk.server.fonts

interface Font {

    val fontWidth: Int

    val fontHeight: Int

    val xOffset: Int

    val yOffset: Int

    val font: IntArray

}