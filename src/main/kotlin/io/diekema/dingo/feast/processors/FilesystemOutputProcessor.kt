/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.processors

import io.diekema.dingo.feast.Asset
import io.diekema.dingo.feast.Exchange
import org.apache.commons.lang3.StringUtils

import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Created by rdiekema on 8/23/16.
 */
class FilesystemOutputProcessor(private val outputPath: String) : Processor {

    @Throws(IOException::class)
    override fun process(exchange: Exchange) {
        if (!StringUtils.isEmpty(outputPath)) {
            for (asset in exchange.assets) {
                val absoluteOutPut = Paths.get(outputPath).toAbsolutePath()

                if (!Files.exists(absoluteOutPut)) {
                    Files.createDirectories(absoluteOutPut)
                }

                val outputFilename = StringBuilder(asset.name)
                if (asset.revision != null) {
                    outputFilename.append(asset.revision)
                }
                outputFilename.append(".").append(asset.extension)

                val absolutePath = absoluteOutPut.toString() + File.separator + outputFilename.toString()
                val fileWriter = FileWriter(absolutePath)
                fileWriter.write(asset.content)
                fileWriter.flush()
                fileWriter.close()
                asset.currentPath = Paths.get(absolutePath)
            }
        }
    }
}
