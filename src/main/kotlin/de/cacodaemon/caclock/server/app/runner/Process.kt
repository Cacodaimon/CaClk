package de.cacodaemon.caclock.server.app.runner

import jdk.nashorn.internal.runtime.ScriptingFunctions.readLine
import java.io.InputStreamReader
import java.io.BufferedReader
import org.apache.commons.io.FileUtils.waitFor
import org.apache.commons.io.IOUtils


class Process {

    data class Result (val code: Int, val stdOut: String, val stdErr: String);

    public fun run(command: String): Result {
        val p = Runtime
                .getRuntime()
                .exec(command)

        return Result(
                p.waitFor(),
                IOUtils.toString(p.inputStream, "UTF-8"),
                IOUtils.toString(p.errorStream, "UTF-8")
        )
    }
}