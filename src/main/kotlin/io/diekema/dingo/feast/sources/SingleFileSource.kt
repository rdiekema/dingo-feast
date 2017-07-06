package io.diekema.dingo.feast.sources

import io.diekema.dingo.feast.Asset
import io.diekema.dingo.feast.Exchange
import org.apache.commons.io.IOUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Collections

import java.nio.charset.StandardCharsets.UTF_8

/**
 * Created by rdiekema on 11/23/16.
 */
class SingleFileSource(internal var absolutePath: String) : Source {

    @Throws(IOException::class)
    override fun collect(): Exchange {
        val singleFilePath = Paths.get(absolutePath)
        val filePayload = FilePayload(singleFilePath)
        log.info(String.format("Found %d files.", 1))
        val exchange = Exchange()
        exchange.enrich(listOf(Asset(filePayload.name, IOUtils.toString(filePayload.inputStream, UTF_8), filePayload.path.toString())))

        return exchange
    }

    internal class FilePayload @Throws(FileNotFoundException::class)
    constructor(var path: Path) {
        var name: String
        var inputStream: InputStream

        init {
            this.name = path.fileName.toString()
            this.inputStream = FileInputStream(path.toFile())
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(SingleFileSource::class.java.name)
    }
}
