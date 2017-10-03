/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.sources

import io.diekema.dingo.feast.Asset
import io.diekema.dingo.feast.Exchange
import io.diekema.dingo.feast.Pipeline
import org.slf4j.LoggerFactory
import java.io.IOException
import java.util.*

/**
 * Created by rdiekema on 9/23/16.
 */
class PipelineAggregateSource(private val sourcePipelines: Array<Pipeline>) : Source {

    @Throws(IOException::class)
    override fun collect(): Exchange {
        val assetList = ArrayList<Asset>()

        sourcePipelines
                .map { pipe ->
                    assetList.addAll(pipe.run())
                }


        val exchange = Exchange()
        exchange.assets = assetList
        return exchange
    }

    companion object {

        private val log = LoggerFactory.getLogger(PipelineAggregateSource::class.java.name)
    }
}
