/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.processors.html;

import io.diekema.dingo.feast.Asset;
import io.diekema.dingo.feast.Exchange;
import io.diekema.dingo.feast.Features;
import io.diekema.dingo.feast.Pipeline;
import io.diekema.dingo.feast.processors.Processor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

/**
 * Created by rdiekema on 8/24/16.
 */
public class HtmlReplaceAssetProcessor implements Processor {

    private Pipeline assetTargets;

    public HtmlReplaceAssetProcessor(Pipeline assetTargets) {
        this.assetTargets = assetTargets;
    }

    @Override
    public void process(Exchange exchange) throws IOException {
        List<Asset> assetList = exchange.getAssets();

        List<Asset> targetFiles = assetTargets.run();

        for (Asset asset : targetFiles) {
            Document document = Jsoup.parse(asset.getContent());

            for (Asset replacementAsset : assetList) {
                Elements elements = document.select("." + replacementAsset.getName());

                if (elements.size() > 0) {

                    Element replacement = null;
                    switch (replacementAsset.getExtension()) {
                        case Features.Format.js:
                            Attributes scriptAttributes = new Attributes();
                            Attribute attribute = new Attribute("src", replacementAsset.getName() + "." + replacementAsset.getExtension());
                            scriptAttributes.put(attribute);
                            replacement = new Element(Tag.valueOf("script"), "", scriptAttributes);
                            break;
                        case Features.Format.css:
                            Attributes linkAttributes = new Attributes();
                            linkAttributes.put(new Attribute("rel", "stylesheet"));
                            linkAttributes.put(new Attribute("type", "text/css"));
                            linkAttributes.put(new Attribute("href", replacementAsset.getName() + "." + replacementAsset.getExtension()));
                            replacement = new Element(Tag.valueOf("link"), "", linkAttributes);
                            break;
                    }

                    if (replacement != null) {
                        elements.first().replaceWith(replacement);

                        for (int i = 1; i < elements.size(); i++) {
                            elements.get(i).remove();
                        }
                    }

                }

            }

            asset.setContent(document.html());
        }

        exchange.setAssets(targetFiles);
    }
}
