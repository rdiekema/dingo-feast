/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

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
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.Collections
import java.util.LinkedList
import java.util.concurrent.atomic.AtomicInteger

import java.nio.charset.StandardCharsets.UTF_8

/**
 * Created by rdiekema on 9/25/16.
 */
class SimpleFileSystemSource(private val inputPath: String, private val inputPatternAndSyntax: String) : Source {

    @Throws(IOException::class)
    override fun collect(): Exchange {
        val pathMatcher = FileSystems.getDefault().getPathMatcher(inputPatternAndSyntax)
        val fileCount = AtomicInteger(0)
        val payloads = LinkedList<FilePayload>()

        Files.walkFileTree(FileSystems.getDefault().getPath(inputPath), emptySet<FileVisitOption>(), 1, object : SimpleFileVisitor<Path>() {
            @Throws(IOException::class)
            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                if (pathMatcher.matches(file)) {
                    fileCount.incrementAndGet()
                    payloads.add(FilePayload(file))
                }

                return FileVisitResult.CONTINUE
            }

            @Throws(IOException::class)
            override fun visitFileFailed(file: Path, exc: IOException?): FileVisitResult {
                return FileVisitResult.CONTINUE
            }
        })

        log.info(String.format("Found %d files.", fileCount.toInt()))

        val assets = LinkedList<Asset>()
        for (filePayload in payloads) {
            assets.add(Asset(filePayload.name, IOUtils.toString(filePayload.inputStream, UTF_8), filePayload.path.toString()))
        }

        val exchange = Exchange()
        exchange.enrich(assets)

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

        private val log = LoggerFactory.getLogger(SimpleFileSystemSource::class.java.name)
    }

}
