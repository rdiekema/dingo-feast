/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.processors.js

import com.google.javascript.jscomp.SourceFile
import io.diekema.dingo.feast.Asset
import io.diekema.dingo.feast.Exchange
import io.diekema.dingo.feast.processors.NoOpMessageProcessor

import java.io.IOException
import java.io.StringReader
import java.util.ArrayList
import java.util.Collections

/**
 * Created by rdiekema on 8/23/16.
 */
class JavascriptProcessor : NoOpMessageProcessor() {

    @Throws(IOException::class)
    override fun process(exchange: Exchange) {
        val assetList = exchange.assets

        if (!assetList.isEmpty()) {
            val closureCompilerContext = ClosureCompilerContext()

            val sourceFiles = ArrayList<SourceFile>()
            for (asset in assetList) {
                val file = SourceFile.fromReader(asset.name, StringReader(asset.content))
                sourceFiles.add(file)
            }

            val minified = closureCompilerContext.minify(sourceFiles)

            exchange.assets = mutableListOf(Asset(null, minified, null, "js"))
        }
    }
}
