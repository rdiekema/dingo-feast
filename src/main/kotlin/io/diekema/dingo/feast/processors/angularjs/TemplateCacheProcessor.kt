/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.processors.angularjs

import io.diekema.dingo.feast.Asset
import io.diekema.dingo.feast.Exchange
import io.diekema.dingo.feast.Features
import io.diekema.dingo.feast.processors.Processor

import java.io.IOException

/**
 * Created by rdiekema on 9/24/16.
 */
class TemplateCacheProcessor(private val appName: String, private val outputFilename: String) : Processor {

    @Throws(IOException::class)
    override fun process(exchange: Exchange) {
        val assetList = exchange.assets

        val cachedTemplates = StringBuilder().append("\n")
        val individualTemplates = StringBuilder()
        for (asset in assetList) {
            individualTemplates.append("\n\t").append(String.format(TEMPLATE_CACHE_PUT_TEMPLATE, asset.name + "." + asset.extension, asset.content?.replace("'", "\\'")?.replace("\n", " ")))
        }

        cachedTemplates.append(String.format(MODULE_TEMPLATE, appName, appName, String.format(WRAPPING_FUNCTION_TEMPLATE, appName, individualTemplates.append("\n").toString())))
        assetList.clear()
        assetList.add(Asset(outputFilename, cachedTemplates.toString(), null, Features.Format.js))
    }

    companion object {
        private val MODULE_TEMPLATE = "'use strict'; var %s = angular.module('%s'); %s"
        private val WRAPPING_FUNCTION_TEMPLATE = "%s.run(['\$templateCache',function(\$templateCache) {%s}]);"
        private val TEMPLATE_CACHE_PUT_TEMPLATE = "\$templateCache.put('%s','%s');"
    }
}
