/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.sources

import io.diekema.dingo.feast.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.io.IOException
import java.util.*
import java.util.concurrent.*

/**
 * Created by rdiekema on 9/23/16.
 */
class PipelineAggregateSource(sourcePipelines: Array<Pipeline>) : Source {

    private val sourcePipelines: Array<Pipeline> = sourcePipelines
    private val completionService: CompletionService<List<Asset>>

    init {
        this.completionService = ExecutorCompletionService<List<Asset>>(Executors.newFixedThreadPool(sourcePipelines.size))
    }

    @Throws(IOException::class)
    override fun collect(): Exchange {
        val submitted = ArrayList<Future<List<Asset>>>()
        for (p in sourcePipelines) {
            submitted.add(completionService.submit(PipelineCallable(p)))
        }

        val assetList = ArrayList<Asset>()
        for (i in submitted.indices) {
            try {
                val result = completionService.take()
                assetList.addAll(result.get())
            } catch (e: InterruptedException) {
                log.error(e.message)
            } catch (e: ExecutionException) {
                log.error(e.message)
            }

        }

        val exchange = Exchange()
        exchange.assets = assetList
        return exchange
    }

    companion object {

        private val log = LoggerFactory.getLogger(PipelineAggregateSource::class.java.name)
    }
}
