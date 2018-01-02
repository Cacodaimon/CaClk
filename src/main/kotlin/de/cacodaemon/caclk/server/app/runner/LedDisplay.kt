package de.cacodaemon.caclk.server.app.runner

import de.cacodaemon.caclk.server.fonts.Font
import de.cacodaemon.caclk.server.fonts.Font8x8
import de.cacodaemon.rpiws28114j.Color
import de.cacodaemon.rpiws28114j.WS2811
import java.lang.Math.*

object LedDisplay {

    private val width = 8

    private val height = 8

    private val displayPixelCount = width * height

    private val numDisplays = 4

    private var font: Font = Font8x8()

    @Synchronized
    public fun putPixel(x: Int, y: Int, color: Color): LedDisplay {
        if (x < 0 || x > width * numDisplays) {
            return this
        }

        if (y < 0 || y > height) {
            return this
        }

        val display = floor(x / width.toDouble())
        val displayOffset = displayPixelCount * display

        WS2811.setPixel((displayOffset + (y - display) * width + x).toInt(), darkenColor(color))
        return this
    }

    public fun line(xA: Int, yA: Int, xB: Int, yB: Int, color: Color): LedDisplay {
        var x0 = xA
        var y0 = yA
        val dx = abs(xB - x0)
        val sx = if (x0 < xB) 1 else -1
        val dy = -abs(yB - y0)
        val sy = if (y0 < yB) 1 else -1
        var err = dx + dy
        var e2: Int /* error value e_xy */

        while (true) {
            putPixel(x0, y0, color)
            if (x0 == xB && y0 == yB) break
            e2 = 2 * err
            if (e2 > dy) {
                err += dy
                x0 += sx
            }
            if (e2 < dx) {
                err += dx
                y0 += sy
            }
        }
        return this
    }

    public fun putString(string: String, color: Color, offsetX: Int, offsetY: Int): LedDisplay {
        for (i in 0 until string.length) {
            putChar(string[i], color, i * font.fontWidth + offsetX, offsetY)
        }

        return this
    }

    public fun putString(string: String, color: Color, offsetX: Int): LedDisplay {
        return putString(string, color, offsetX, 0)
    }

    public fun putString(string: String, color: Color): LedDisplay {
        return putString(string, color, 0)
    }

    public fun clear(): LedDisplay {
        return fill(Color.BLACK)
    }

    @Synchronized
    public fun fill(color: Color): LedDisplay {
        for (i in 0..displayPixelCount * numDisplays) {
            WS2811.setPixel(i, darkenColor(color))
        }

        return this
    }

    @Synchronized
    public fun render(): LedDisplay {
        WS2811.render()
        return this
    }

    public fun setFont(font: de.cacodaemon.caclk.server.fonts.Font): LedDisplay {
        this.font = font
        return this
    }

    private fun darkenColor(color: Color): Color {
        if (Brightness.value == 1.0 || Math.abs(Brightness.value - 1.0) < 0.01) {
            return color
        }

        val r = color.rgb and 0x0000FF
        val g = color.rgb and 0x00FF00 shr 8
        val b = color.rgb and 0xFF0000 shr 16

        return Color(
                Math.ceil(r * Brightness.value).toInt(),
                Math.ceil(g * Brightness.value).toInt(),
                Math.ceil(b * Brightness.value).toInt()
        )
    }

    private fun putChar(char: Char, color: Color, offsetX: Int, offsetY: Int): LedDisplay {
        val start = char.toInt()

        if (start > 255) {
            return this
        }

        var y = offsetY + font.yOffset
        for (i in start * font.fontHeight..start * font.fontHeight + font.fontHeight) {
            Integer
                    .toBinaryString(font.font[i])
                    .split("")
                    .reversed()
                    .map { pixelColor -> if (pixelColor == "1") color else null }
                    .forEachIndexed { x, pixelColor ->
                        if (pixelColor == null) {
                            return@forEachIndexed
                        }
                        LedDisplay.putPixel(font.fontWidth - x + offsetX + font.xOffset, y, pixelColor)
                    }
            y++
        }

        return this
    }
}
