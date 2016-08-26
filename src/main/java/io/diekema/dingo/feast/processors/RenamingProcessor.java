package io.diekema.dingo.feast.processors;

import io.diekema.dingo.feast.Asset;
import io.diekema.dingo.feast.Exchange;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by rdiekema on 8/24/16.
 */
public class RenamingProcessor implements Processor {

    private String name;

    public RenamingProcessor(String name) {
        this.name = name;
    }

    @Override
    public void process(Exchange exchange) throws IOException {
        List<Asset> assetList = exchange.getAssets();

        if (assetList.size() == 1) {
            for (Asset asset : assetList) {
                exchange.setAssets(Collections.singletonList(new Asset(name, asset.getContent(), null)));
            }
        } else if (assetList.size() > 1) {
            for (int i = 0; i < assetList.size(); i++) {
                assetList.add(new Asset(String.valueOf(i) + "_" + name, assetList.get(i).getContent(), null));
                assetList.remove(i);
            }
        }
    }
}
