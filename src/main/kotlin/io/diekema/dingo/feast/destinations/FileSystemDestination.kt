package io.diekema.dingo.feast.destinations

import io.diekema.dingo.feast.Exchange
import io.diekema.dingo.feast.Features
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import org.apache.commons.lang3.StringUtils
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by rdiekema on 9/23/16.
 */
class FileSystemDestination(internal var outputPath: String) : Destination {

    @Throws(IOException::class)
    override fun deliver(exchange: Exchange): Boolean {
        if (!StringUtils.isEmpty(outputPath)) {
            runBlocking {
                exchange.assets.map {
                    async(CommonPool) {
                        val absoluteOutPut = Paths.get(outputPath).toAbsolutePath()

                        if (!Files.exists(absoluteOutPut)) {
                            Files.createDirectories(absoluteOutPut)
                        }

                        val absolutePath = absoluteOutPut.toString() + File.separator + it.name + Features.DOT + it.extension
                        val fileWriter = FileWriter(absolutePath)
                        fileWriter.write(it.content!!)
                        fileWriter.flush()
                        fileWriter.close()
                        it.currentPath = Paths.get(absolutePath)
                    }
                }
            }

            return true
        }
        return false
    }
}
