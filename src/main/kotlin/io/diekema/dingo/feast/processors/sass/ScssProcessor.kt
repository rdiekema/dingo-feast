package io.diekema.dingo.feast.processors.sass

import com.cathive.sass.*
import io.diekema.dingo.feast.Asset
import io.diekema.dingo.feast.Exchange
import io.diekema.dingo.feast.processors.Processor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Collections

/**
 * Created by rdiekema on 11/23/16.
 */
class ScssProcessor() : Processor {
    private val log = LoggerFactory.getLogger(ScssProcessor::class.java.name)

    var includeDirs: Array<String> = arrayOf()

    constructor(dirs: Array<String>) : this() {
        includeDirs = dirs
    }

    @Throws(IOException::class)
    override fun process(exchange: Exchange) {
        val assets = exchange.assets

        val rootSource = assets[0]

        // Creates a new sass file context.
        val ctx = SassFileContext.create(rootSource.originalPath!!)
        val options = ctx.options

        options.pushIncludePath(rootSource.originalPath!!.parent)

        for (includeDir in includeDirs) {
            options.pushIncludePath(Paths.get(includeDir))
        }

        options.outputStyle = SassOutputStyle.NESTED
        options.isIndentedSyntaxSrc = rootSource.extension == "sass"
        // any other options supported by libsass including source map stuff can be configured
        // as well here.

        // Will print the compiled CSS contents to the console. Use a FileOutputStream
        // or some other fancy mechanism to redirect the output to wherever you want.
        try {
            val byteArrayOutputStream = ByteArrayOutputStream()
            ctx.compile(byteArrayOutputStream)
            val compiledContent = String(byteArrayOutputStream.toByteArray())

            exchange.assets = mutableListOf(Asset(null, compiledContent, null, null, "css", null))
        } catch (e: SassCompilationException) {
            // Will print the error code, filename, line, column and the message provided
            // by libsass to the standard error output.
            log.error(e.message)
        } catch (e: IOException) {
            log.error(String.format("Compilation failed: %s", e.message))
        }

    }

}
