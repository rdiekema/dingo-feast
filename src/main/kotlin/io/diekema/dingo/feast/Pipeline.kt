/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast

import io.diekema.dingo.feast.destinations.Destination
import io.diekema.dingo.feast.processors.*
import io.diekema.dingo.feast.processors.html.HtmlReplaceAssetProcessor
import io.diekema.dingo.feast.sources.PipelineAggregateSource
import io.diekema.dingo.feast.sources.PipelineSource
import io.diekema.dingo.feast.sources.Source

import java.io.IOException
import java.util.LinkedList
import java.util.logging.Logger

/**
 * Created by rdiekema on 8/23/16.
 */
class Pipeline {

    private var source: Source? = null
    private val joints = LinkedList<Processor>()
    private var destination: Destination? = null

    fun from(pipeline: Pipeline): Pipeline {
        this.source = PipelineSource(pipeline)
        return this
    }

    fun from(source: Source): Pipeline {
        this.source = source
        return this
    }

    fun from(pipelines: Array<Pipeline>): Pipeline {
        this.source = PipelineAggregateSource(pipelines)
        return this
    }

    fun to(vararg pipeline: Pipeline) {
        for (p in pipeline) {
            p.from(PipelineSource(this))
        }
    }

    fun to(destination: Destination): Pipeline {
        this.destination = destination
        return this
    }

    fun `as`(name: String): Pipeline {
        joints.add(RenamingProcessor(name))
        return this
    }

    fun replace(from: Pipeline): Pipeline {
        joints.add(HtmlReplaceAssetProcessor(from))
        return this
    }

    fun file(outputPath: String): Pipeline {
        joints.add(FilesystemOutputProcessor(outputPath))
        return this
    }

    fun enrich(pipeline: Pipeline): Pipeline {
        joints.add(EnrichingProcessor(pipeline))
        return this
    }

    fun process(processor: Processor): Pipeline {
        joints.add(processor)
        return this
    }

    fun log(): Pipeline {
        joints.add(LoggingProcessor())
        return this
    }

    @Throws(IOException::class)
    fun run(): MutableList<Asset> {
        val exchange: Exchange
        if (source != null) {
            exchange = source!!.collect()
        } else {
            exchange = Exchange()
        }

        for (processor in joints) {
            processor.process(exchange)
        }

        if (destination != null) {
            destination!!.deliver(exchange)
        }

        return exchange.assets
    }

    companion object {

        private val log = Logger.getLogger(Pipeline::class.java.name)
    }

}
