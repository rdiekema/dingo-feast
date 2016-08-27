package io.diekema.dingo.feast.processors.html;

import io.diekema.dingo.feast.Asset;
import io.diekema.dingo.feast.Exchange;
import io.diekema.dingo.feast.Pipeline;
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
public class HtmlReplaceAssetProcessor extends ReplaceAssetProcessor {

    public HtmlReplaceAssetProcessor(Pipeline assetTargets, String target) {
        super(target, assetTargets);
    }

    @Override
    public void process(Exchange exchange) throws IOException {
        List<Asset> assetList = exchange.getAssets();

        List<Asset> targetFiles = getEnrichingPipeline().run();

        for(Asset asset : targetFiles){
            Document document = Jsoup.parse(asset.getContent());
            Elements elements = document.select(getTarget());

            for(Asset replacementAsset : assetList) {
                Attributes attributes = new Attributes();
                Attribute attribute = new Attribute("src", replacementAsset.getName());
                attributes.put(attribute);
                Element replacement = new Element(Tag.valueOf("script"), "", attributes);

                Element parent = elements.first().parent();
                elements.remove();

                parent.appendChild(replacement);
            }

            asset.setContent(document.html());
        }

        exchange.setAssets(targetFiles);
    }
}
