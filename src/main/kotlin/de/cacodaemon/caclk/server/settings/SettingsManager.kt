package de.cacodaemon.caclk.server.settings

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import de.cacodaemon.rpiws28114j.StripType.WS2811_STRIP_GBR
import org.apache.commons.io.FileUtils
import java.io.FileReader
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

object SettingsManager {

    private val fileName = "settings.json"

    private val default = Settings(
            port = 8888,
            maxThreads = 4,
            gpioPin = 10,
            ledCount = 256,
            stripType = WS2811_STRIP_GBR,
            invert = false,
            brightness = 255,
            softBrightness = 0.5
    )

    lateinit var settings: Settings

    fun load() {
        if (!Files.exists(Paths.get(fileName))) {
            save(default)
        }

        settings = Gson().fromJson(FileReader(fileName), Settings::class.java)
    }

    fun save() {
        save(settings)
    }

    private fun save(settings: Settings) {
        val json = GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(settings)

        FileUtils.writeStringToFile(Paths.get(fileName).toFile(), json, Charset.forName("UTF-8"))
    }
}