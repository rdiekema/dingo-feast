/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.processors.less

import com.github.sommeri.less4j.Less4jException
import com.github.sommeri.less4j.LessCompiler
import com.github.sommeri.less4j.core.ThreadUnsafeLessCompiler
import io.diekema.dingo.feast.Asset
import io.diekema.dingo.feast.Exchange
import io.diekema.dingo.feast.processors.NoOpMessageProcessor
import io.diekema.dingo.feast.processors.Processor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.io.IOException
import java.util.Arrays

/**
 * Created by rdiekema on 9/9/16.
 */
class LessProcessor : Processor {

    @Throws(IOException::class)
    override fun process(exchange: Exchange) {

        for (asset in exchange.assets) {
            try {
                val compiler = ThreadUnsafeLessCompiler()
                val result = compiler.compile(asset.originalPath?.toFile())
                exchange.assets = Arrays.asList(Asset(asset.name, result.css, null, "css"), Asset(asset.name, result.sourceMap, null, "map"))
            } catch (e: Less4jException) {
                log.error(e.message)
            }

        }
    }

    companion object {

        private val log = LoggerFactory.getLogger(LessProcessor::class.java.name)
    }
}
