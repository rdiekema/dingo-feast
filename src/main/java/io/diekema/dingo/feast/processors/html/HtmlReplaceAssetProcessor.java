package io.diekema.dingo.feast.processors.html;

import io.diekema.dingo.feast.*;
import io.diekema.dingo.feast.processors.Processor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import javax.swing.text.html.HTML;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by rdiekema on 8/24/16.
 */
public class HtmlReplaceAssetProcessor implements Processor {

    private String target;
    private Pipeline assetTargets;

    public HtmlReplaceAssetProcessor(Pipeline assetTargets, String target) {
        this.target = target;
        this.assetTargets = assetTargets;
    }

    @Override
    public void process(Exchange exchange) throws IOException {
        List<Asset> assetList = exchange.getAssets();

        List<Asset> targetFiles = assetTargets.run();

        for(Asset asset : targetFiles){
            Document document = Jsoup.parse(asset.getContent());
            Elements elements = document.select(target);

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
