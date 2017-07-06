/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.processors

import io.diekema.dingo.feast.*
import org.apache.commons.io.IOUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.LinkedList
import java.util.concurrent.atomic.AtomicInteger

import java.nio.charset.StandardCharsets.UTF_8

/**
 * Created by rdiekema on 8/24/16.
 */
class FileSystemInputProcessor(private val inputPath: String, private val inputPatternAndSyntax: String) : Processor {

    @Throws(IOException::class)
    override fun process(exchange: Exchange) {
        val pathMatcher = FileSystems.getDefault().getPathMatcher(inputPatternAndSyntax)
        val fileCount = AtomicInteger(0)
        val payloads = LinkedList<FilePayload>()

        Files.walkFileTree(FileSystems.getDefault().getPath(inputPath), object : SimpleFileVisitor<Path>() {
            @Throws(IOException::class)
            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                if (pathMatcher.matches(file)) {
                    fileCount.incrementAndGet()
                    payloads.add(FilePayload(file.toString()))
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
            assets.add(Asset(filePayload.name, IOUtils.toString(filePayload.inputStream, UTF_8), filePayload.path))
        }

        exchange.enrich(assets)
    }

    internal class FilePayload @Throws(FileNotFoundException::class)
    constructor(var path: String?) {
        var name: String
        var inputStream: InputStream

        init {
            this.name = Paths.get(path).fileName.toString()
            this.inputStream = FileInputStream(Paths.get(path).toFile())
        }
    }

    companion object {

        private val log = LoggerFactory.getLogger(FileSystemInputProcessor::class.java.name)
    }
}
