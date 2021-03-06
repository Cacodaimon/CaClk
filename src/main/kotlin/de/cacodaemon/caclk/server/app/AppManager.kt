package de.cacodaemon.caclk.server.app

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.apache.commons.io.FileUtils
import java.io.FileReader
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.util.*


object AppManager {

    private val fileName = "apps.json"

    private lateinit var apps: MutableList<App>

    @Synchronized
    fun load() {
        if (!Files.exists(Paths.get(fileName))) {
            save(emptyList<App>())
        }

        val listType = object : TypeToken<List<App>>() {}.type
        apps = Gson().fromJson(FileReader(fileName), listType)
    }

    @Synchronized
    fun all(): List<App> {
        return ArrayList(apps)
    }

    @Synchronized
    fun get(id: Int): App? {
        return apps.find { (id1) -> id1 == id }
    }

    @Synchronized
    fun add(app: App) {
        app.id = 1
        apps.maxBy(App::id)?.let { a -> app.id = a.id + 1 }
        apps.add(app)
    }

    @Synchronized
    fun edit(id: Int, editedApp: App) {
        val app = apps.find { (id1) -> id1 == id }
        apps[apps.indexOf(app)] = editedApp
    }

    @Synchronized
    fun delete(id: Int) {
        val app = apps.find { (id1) -> id1 == id }
        apps.remove(app)
    }

    @Synchronized
    fun save() {
        save(apps)
    }

    @Synchronized
    fun getAutoStart(): App? {
        return apps.find { app -> app.autoStart }
    }

    @Synchronized
    private fun save(apps: List<App>) {
        val json = GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(apps)

        FileUtils.writeStringToFile(Paths.get(fileName).toFile(), json, Charset.forName("UTF-8"))
    }
}