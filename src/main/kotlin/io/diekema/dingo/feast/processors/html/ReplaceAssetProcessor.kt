/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.processors.html

import io.diekema.dingo.feast.Exchange
import io.diekema.dingo.feast.Pipeline
import io.diekema.dingo.feast.processors.Processor

import java.io.IOException

/**
 * Created by rdiekema on 8/26/16.
 */
class ReplaceAssetProcessor(var target: String?, var enrichingPipeline: Pipeline?) : Processor {

    @Throws(IOException::class)
    override fun process(exchange: Exchange) {

    }
}
