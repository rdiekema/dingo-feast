package io.diekema.dingo.feast.processors.js;

import com.google.javascript.jscomp.SourceFile;
import io.diekema.dingo.feast.Asset;
import io.diekema.dingo.feast.Exchange;
import io.diekema.dingo.feast.Message;
import io.diekema.dingo.feast.processors.AbstractMessageProcessor;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by rdiekema on 8/23/16.
 */
public class JavascriptProcessor extends AbstractMessageProcessor {

    @Override
    public void process(Exchange exchange) throws IOException {
        List<Asset> assetList = exchange.getAssets();

        ClosureCompilerContext closureCompilerContext = new ClosureCompilerContext();

        List<SourceFile> sourceFiles = new ArrayList<>();
        for(Asset asset : assetList){
            SourceFile file =  SourceFile.fromReader(asset.getName(), new StringReader(asset.getContent()));
            sourceFiles.add(file);
        }

        String minified = closureCompilerContext.minify(sourceFiles);

        exchange.setAssets(Collections.singletonList(new Asset(null, minified, null, "js")));
    }
}
