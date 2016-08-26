package io.diekema.dingo.feast.processors;

import io.diekema.dingo.feast.Asset;
import io.diekema.dingo.feast.DSL;
import io.diekema.dingo.feast.Exchange;
import io.diekema.dingo.feast.Message;

import java.util.*;

/**
 * Created by rdiekema on 8/23/16.
 */
public class ConcatenatingProcessor extends AbstractMessageProcessor {
    @Override
    public void process(Exchange exchange) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Asset asset : (List<Asset>)exchange.getAssets()) {
            stringBuilder.append(asset.getContent()).append("\n");
        }

        exchange.setAssets(Collections.singletonList(new Asset(null, stringBuilder.toString(), null)));
    }
}
