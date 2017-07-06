/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.processors

import io.diekema.dingo.feast.Asset
import io.diekema.dingo.feast.Exchange
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.io.IOException

/**
 * Created by rdiekema on 9/23/16.
 */
class LoggingProcessor : Processor {

    @Throws(IOException::class)
    override fun process(exchange: Exchange) {
        for (asset in exchange.assets) {
            log.info(asset.toString())
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(LoggingProcessor::class.java.name)
    }
}
