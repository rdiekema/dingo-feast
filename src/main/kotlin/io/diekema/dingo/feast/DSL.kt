/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast

import io.diekema.dingo.feast.destinations.Destination
import io.diekema.dingo.feast.destinations.FileSystemDestination
import io.diekema.dingo.feast.processors.VersionProcessor
import io.diekema.dingo.feast.processors.angularjs.TemplateCacheProcessor
import io.diekema.dingo.feast.processors.image.PngProcessor
import io.diekema.dingo.feast.processors.js.JavascriptProcessor
import io.diekema.dingo.feast.processors.less.LessProcessor
import io.diekema.dingo.feast.processors.sass.ScssProcessor
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
        fun pipeline(): Pipeline {
            return Pipeline()
        }

        @JvmStatic
        fun templateCache(appName: String, outputFilename: String): TemplateCacheProcessor {
            return TemplateCacheProcessor(appName, outputFilename)
        }

        @JvmStatic
        fun js(): JavascriptProcessor {
            return JavascriptProcessor()
        }

        @JvmStatic
        fun less(): LessProcessor {
            return LessProcessor()
        }

        @JvmStatic
        fun version(): VersionProcessor {
            return VersionProcessor()
        }

        @JvmStatic
        fun sass() : ScssProcessor {
            return ScssProcessor()
        }

        @JvmStatic
        fun sass(dirs: Array<String>) : ScssProcessor {
            return ScssProcessor(dirs)
        }

        @JvmStatic
        fun sprite(dir: String) : PngProcessor {
            return PngProcessor(dir)
        }
    }
}
