/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.processors

import io.diekema.dingo.feast.Exchange
import io.diekema.dingo.feast.Pipeline

import java.io.IOException

/**
 * Created by rdiekema on 9/9/16.
 */
class EnrichingProcessor(private val pipeline: Pipeline) : NoOpMessageProcessor() {

    @Throws(IOException::class)
    override fun process(exchange: Exchange) {
        exchange.enrich(pipeline.run())
    }
}
