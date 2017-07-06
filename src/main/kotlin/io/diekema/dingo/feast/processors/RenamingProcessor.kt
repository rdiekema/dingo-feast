/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.processors

import io.diekema.dingo.feast.Asset
import io.diekema.dingo.feast.Exchange

import java.io.IOException
import java.util.Collections
import java.util.HashMap

/**
 * Created by rdiekema on 8/24/16.
 */
class RenamingProcessor(private val prefix: String) : Processor {

    @Throws(IOException::class)
    override fun process(exchange: Exchange) {
        val assetList = exchange.assets

        if (assetList.size == 1) {
            for (asset in assetList) {
                exchange.assets = mutableListOf(Asset(prefix + if (asset.name != null) asset.name else "", asset.content, null, asset.extension))
            }
        } else if (assetList.size > 1) {
            val prefixExtensionCounts = HashMap<String, Int?>()

            for (asset in assetList) {
                val newName: String

                if (!prefixExtensionCounts.containsKey(prefix + asset.extension)) {
                    prefixExtensionCounts.put(prefix + asset.extension, 1)
                    newName = prefix
                } else {
                    prefixExtensionCounts.put(prefix + asset.extension, prefixExtensionCounts[prefix + asset.extension]?.plus(1))
                    newName = prefix + prefixExtensionCounts[prefix + asset.extension]
                }

                assetList[assetList.indexOf(asset)] = Asset(newName, asset.content, null, asset.extension)
            }
        }
    }


}
