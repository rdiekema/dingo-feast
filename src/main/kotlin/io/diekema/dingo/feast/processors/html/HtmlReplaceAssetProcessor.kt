/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.processors.html

import io.diekema.dingo.feast.Exchange
import io.diekema.dingo.feast.Features
import io.diekema.dingo.feast.Pipeline
import io.diekema.dingo.feast.processors.Processor
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import org.jsoup.Jsoup
import org.jsoup.nodes.Attribute
import org.jsoup.nodes.Attributes
import org.jsoup.nodes.Element
import org.jsoup.parser.Tag
import java.io.IOException

/**
 * Created by rdiekema on 8/24/16.
 */
class HtmlReplaceAssetProcessor(private val assetTargets: Pipeline) : Processor {

    @Throws(IOException::class)
    override fun process(exchange: Exchange) {
        val assetList = exchange.assets

        val targetFiles = assetTargets.run()

        for (asset in targetFiles) {
            val document = Jsoup.parse(asset.content)

            runBlocking {
                assetList.map { replacementAsset ->
                    async(CommonPool) {
                        val elements = document.select("." + replacementAsset.name)

                        if (elements.size > 0) {

                            var replacement: Element? = null
                            when (replacementAsset.extension) {
                                Features.Format.js -> {
                                    val scriptAttributes = Attributes()
                                    val attribute = Attribute("src", replacementAsset.name + replacementAsset.revision + "." + replacementAsset.extension)
                                    scriptAttributes.put(attribute)
                                    replacement = Element(Tag.valueOf("script"), "", scriptAttributes)
                                }
                                Features.Format.css -> {
                                    val linkAttributes = Attributes()
                                    linkAttributes.put(Attribute("rel", "stylesheet"))
                                    linkAttributes.put(Attribute("type", "text/css"))
                                    linkAttributes.put(Attribute("href", replacementAsset.name + replacementAsset.revision + "." + replacementAsset.extension))
                                    replacement = Element(Tag.valueOf("link"), "", linkAttributes)
                                }
                            }

                            if (replacement != null) {
                                elements.first().replaceWith(replacement)

                                for (i in 1..elements.size - 1) {
                                    elements[i].remove()
                                }
                            }
                        }


                        asset.content = document.html()
                    }
                }.map { it.await() }


            }
        }

        exchange.assets = targetFiles
    }
}
