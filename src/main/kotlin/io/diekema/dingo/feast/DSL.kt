/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast

import io.diekema.dingo.feast.destinations.Destination
import io.diekema.dingo.feast.destinations.FileSystemDestination
import io.diekema.dingo.feast.processors.angularjs.TemplateCacheProcessor
import io.diekema.dingo.feast.sources.RecursiveFileSystemSource
import io.diekema.dingo.feast.sources.SimpleFileSystemSource
import io.diekema.dingo.feast.sources.SingleFileSource
import io.diekema.dingo.feast.sources.Source

/**
 * Created by rdiekema on 9/23/16.
 */
class DSL {
    companion object {

        @JvmStatic
        fun fileSystem(filePath: String, syntaxAndPattern: String): Source {
            return SimpleFileSystemSource(filePath, syntaxAndPattern)
        }

        @JvmStatic
        fun fileSystem(filePath: String, syntaxAndPattern: String, recursive: Boolean): Source {
            if (recursive) {
                return RecursiveFileSystemSource(filePath, syntaxAndPattern)
            } else {
                return fileSystem(filePath, syntaxAndPattern)
            }
        }

        @JvmStatic
        fun singleFile(filePath: String): Source {
            return SingleFileSource(filePath)
        }

        @JvmStatic
        fun fileSystem(destination: String): Destination {
            return FileSystemDestination(destination)
        }

        @JvmStatic
        fun pipe(): Pipeline {
            return Pipeline()
        }

        @JvmStatic
        fun templateCache(appName: String, outputFilename: String): TemplateCacheProcessor {
            return TemplateCacheProcessor(appName, outputFilename)
        }
    }
}