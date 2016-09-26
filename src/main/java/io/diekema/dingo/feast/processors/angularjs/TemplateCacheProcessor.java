/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.processors.angularjs;

import io.diekema.dingo.feast.Asset;
import io.diekema.dingo.feast.Exchange;
import io.diekema.dingo.feast.Features;
import io.diekema.dingo.feast.processors.Processor;

import java.io.IOException;
import java.util.List;

/**
 * Created by rdiekema on 9/24/16.
 */
public class TemplateCacheProcessor implements Processor{
    private static final String MODULE_TEMPLATE = "var %s = angular.module('%s');";
    private static final String WRAPPING_FUNCTION_TEMPLATE = "%s.run(function($templateCache) {%s});";
    private static final String TEMPLATE_CACHE_PUT_TEMPLATE = "$templateCache.put('%s','%s');";

    private String appName;
    private String outputFilename;

    public TemplateCacheProcessor(String appName, String outputFilename) {
        this.appName = appName;
        this.outputFilename = outputFilename;
    }

    @Override
    public void process(Exchange exchange) throws IOException {
        List<Asset> assetList = exchange.getAssets();

        StringBuilder cachedTemplates = new StringBuilder(String.format(MODULE_TEMPLATE, appName, appName)).append("\n");
        StringBuilder individualTemplates = new StringBuilder();
        for(Asset asset : assetList){
            individualTemplates.append("\n\t").append(String.format(TEMPLATE_CACHE_PUT_TEMPLATE, asset.getName() + "." + asset.getExtension(), asset.getContent().replace("'", "\\'").replace("\n", " ")));
        }

        cachedTemplates.append(String.format(WRAPPING_FUNCTION_TEMPLATE, appName, individualTemplates.append("\n").toString()));
        assetList.clear();
        assetList.add(new Asset(outputFilename, cachedTemplates.toString(), null, Features.Format.js));
    }
}
