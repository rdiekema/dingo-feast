/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.io.IOException
import java.util.ArrayList
import java.util.concurrent.Callable

/**
 * Created by rdiekema on 9/23/16.
 */
class PipelineCallable(private val pipeline: Pipeline) : Callable<List<Asset>> {
    private val assets = ArrayList<Asset>()

    fun getAssets(): List<Asset> {
        return assets
    }

    @Throws(Exception::class)
    override fun call(): List<Asset> {
        try {
            assets.addAll(pipeline.run())
        } catch (e: IOException) {
            log.error(e.message)
        }

        return assets
    }

    companion object {

        private val log = LoggerFactory.getLogger(PipelineCallable::class.java.name)
    }
}
