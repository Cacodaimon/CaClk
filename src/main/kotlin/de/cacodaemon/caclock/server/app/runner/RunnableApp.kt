package de.cacodaemon.caclock.server.app.runner

import de.cacodaemon.caclock.server.app.App
import de.cacodaemon.caclock.server.app.AppPermission
import jdk.nashorn.api.scripting.ClassFilter
import jdk.nashorn.api.scripting.NashornScriptEngineFactory
import javax.script.ScriptEngine

class RunnableApp(public val app: App) {

    private var appScriptHeader = "var Integer = Java.type('java.lang.Integer');"

    private val allowedClasses: MutableList<String> = mutableListOf("java.lang.Integer")

    private var engine: ScriptEngine? = null

    private val tearDownCallbacks: MutableList<() -> Unit> = ArrayList<() -> Unit>()

    private val classFilter = ClassFilter { className ->
        return@ClassFilter allowedClasses.contains(className)
    }

    public fun addAllowableClass(className: String) {
        allowedClasses.add(className)
    }

    public fun addAppScriptHeader(scriptHeaderRow: String) {
        appScriptHeader += "$scriptHeaderRow\n"
    }

    public fun getEngine(): ScriptEngine? {
        return engine
    }

    public fun addTearDownCallbacks(tearDownCallback: () -> Unit) {
        tearDownCallbacks.add(tearDownCallback)
    }

    init {
        engine = NashornScriptEngineFactory()
                .getScriptEngine(classFilter)
    }
}