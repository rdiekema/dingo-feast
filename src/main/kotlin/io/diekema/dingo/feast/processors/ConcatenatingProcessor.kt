/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.processors

import io.diekema.dingo.feast.Asset
import io.diekema.dingo.feast.Exchange
import java.nio.file.Paths

import java.util.*

/**
 * Created by rdiekema on 8/23/16.
 */
class ConcatenatingProcessor : NoOpMessageProcessor() {
    override fun process(exchange: Exchange) {
        val stringBuilder = StringBuilder()
        for (asset in exchange.assets) {
            stringBuilder.append(asset.content).append("\n")
        }

        exchange.assets = mutableListOf(Asset("", stringBuilder.toString(), "", exchange.assets[0].extension))
    }
}
