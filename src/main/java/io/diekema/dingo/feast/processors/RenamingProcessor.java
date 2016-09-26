/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.processors;

import io.diekema.dingo.feast.Asset;
import io.diekema.dingo.feast.Exchange;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rdiekema on 8/24/16.
 */
public class RenamingProcessor implements Processor {

    private String prefix;

    public RenamingProcessor(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void process(Exchange exchange) throws IOException {
        List<Asset> assetList = exchange.getAssets();

        if (assetList.size() == 1) {
            for (Asset asset : assetList) {
                exchange.setAssets(Collections.singletonList(new Asset(prefix + (asset.getName() != null ? asset.getName() : ""), asset.getContent(), null, asset.getExtension())));
            }
        } else if (assetList.size() > 1) {
            Map<String, Integer> prefixExtensionCounts = new HashMap<>();

            for(Asset asset : assetList){
                String newName;

                if(!prefixExtensionCounts.containsKey(prefix + asset.getExtension())){
                    prefixExtensionCounts.put(prefix + asset.getExtension(), 1);
                    newName = prefix;
                }
                else{
                    prefixExtensionCounts.put(prefix + asset.getExtension(), prefixExtensionCounts.get(prefix + asset.getExtension()) + 1);
                    newName = prefix + prefixExtensionCounts.get(prefix + asset.getExtension());
                }

                assetList.set(assetList.indexOf(asset), new Asset(newName, asset.getContent(), null, asset.getExtension()));
            }
        }
    }


}
