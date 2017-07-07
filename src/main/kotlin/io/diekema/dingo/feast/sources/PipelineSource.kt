/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.sources

import io.diekema.dingo.feast.Exchange
import io.diekema.dingo.feast.Pipeline
import kotlinx.coroutines.experimental.runBlocking

import java.io.IOException

/**
 * Created by rdiekema on 9/23/16.
 */
class PipelineSource(private val source: Pipeline) : Source {

    @Throws(IOException::class)
    override fun collect(): Exchange {
        val exchange = Exchange()
        exchange.assets = source.run()
        return exchange
    }
}
