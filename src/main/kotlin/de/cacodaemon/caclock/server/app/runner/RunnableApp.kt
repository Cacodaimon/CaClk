package de.cacodaemon.caclock.server.app.runner

import de.cacodaemon.caclock.server.app.App
import de.cacodaemon.caclock.server.app.AppPermission
import jdk.nashorn.api.scripting.ClassFilter
import jdk.nashorn.api.scripting.NashornScriptEngineFactory
import org.apache.log4j.Logger
import javax.script.Invocable
import javax.script.ScriptEngine

class RunnableApp(public val app: App) {

    private val logger = Logger.getRootLogger()

    private var appScriptHeader = "var Integer = Java.type('java.lang.Integer');\n"

    private val allowedClasses: MutableList<String> = mutableListOf("java.lang.Integer")

    private var engine: ScriptEngine? = null

    private val tearDownCallbacks: MutableList<() -> Unit> = ArrayList<() -> Unit>()

    private val startUpCallbacks: MutableList<() -> Unit> = ArrayList<() -> Unit>()

    private val allowedPublicFunctions: MutableList<String> = mutableListOf()

    private val classFilter = ClassFilter { className ->
        return@ClassFilter allowedClasses.contains(className)
    }

    public fun addAllowableClass(className: String) {
        allowedClasses.add(className)
    }

    public fun addAppScriptHeader(scriptHeaderRow: String) {
        appScriptHeader += "$scriptHeaderRow;\n"
    }

    public fun getEngine(): ScriptEngine? {
        return engine
    }

    public fun addTearDownCallbacks(tearDownCallback: () -> Unit) {
        tearDownCallbacks.add(tearDownCallback)
    }

    public fun addStartupCallbacks(tearDownCallback: () -> Unit) {
        startUpCallbacks.add(tearDownCallback)
    }

    public fun allowPublicFunction(name: String) {
        allowedPublicFunctions.add(name)
    }

    public fun callPublicFunction(name: String, vararg args: Any): Any {
        if (!allowedPublicFunctions.contains(name)) {
            logger.warn("Public function \"$name\" is not allowed for app \"${app.name}:${app.version}\"")
        }

        return (engine as Invocable).invokeFunction(name, args)
    }

    init {
        engine = NashornScriptEngineFactory()
                .getScriptEngine(classFilter)
    }

    public fun run() {
        engine!!.eval("$appScriptHeader\n\n${app.code}")

        startUpCallbacks.forEach { callBack -> callBack() }

        (engine as Invocable).invokeFunction("init")
    }

    public fun stop() {
        tearDownCallbacks.forEach { callBack -> callBack() }
    }
}