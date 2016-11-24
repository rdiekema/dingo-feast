/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.processors.js;

import com.google.javascript.jscomp.SourceFile;
import io.diekema.dingo.feast.Asset;
import io.diekema.dingo.feast.Exchange;
import io.diekema.dingo.feast.processors.NoOpMessageProcessor;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by rdiekema on 8/23/16.
 */
public class JavascriptProcessor extends NoOpMessageProcessor {

    @Override
    public void process(Exchange exchange) throws IOException {
        List<Asset> assetList = exchange.getAssets();

        if(!assetList.isEmpty()) {
            ClosureCompilerContext closureCompilerContext = new ClosureCompilerContext();

            List<SourceFile> sourceFiles = new ArrayList<>();
            for (Asset asset : assetList) {
                SourceFile file = SourceFile.fromReader(asset.getName(), new StringReader(asset.getContent()));
                sourceFiles.add(file);
            }

            String minified = closureCompilerContext.minify(sourceFiles);

            exchange.setAssets(Collections.singletonList(new Asset(null, minified, null, "js")));
        }
    }
}
