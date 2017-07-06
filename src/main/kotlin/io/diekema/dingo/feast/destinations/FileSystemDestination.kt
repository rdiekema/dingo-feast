package io.diekema.dingo.feast.destinations

import io.diekema.dingo.feast.Asset
import io.diekema.dingo.feast.Exchange
import io.diekema.dingo.feast.Features
import org.apache.commons.lang3.StringUtils

import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Created by rdiekema on 9/23/16.
 */
class FileSystemDestination(internal var outputPath: String) : Destination {

    @Throws(IOException::class)
    override fun deliver(exchange: Exchange): Boolean {
        if (!StringUtils.isEmpty(outputPath)) {

            for (asset in exchange.assets) {
                val absoluteOutPut = Paths.get(outputPath).toAbsolutePath()

                if (!Files.exists(absoluteOutPut)) {
                    Files.createDirectories(absoluteOutPut)
                }

                val absolutePath = absoluteOutPut.toString() + File.separator + asset.name + Features.DOT + asset.extension
                val fileWriter = FileWriter(absolutePath)
                fileWriter.write(asset.content!!)
                fileWriter.flush()
                fileWriter.close()
                asset.currentPath = Paths.get(absolutePath)
            }
        }
        return false
    }
}
