package io.diekema.dingo.feast.processors;

import io.diekema.dingo.feast.Asset;
import io.diekema.dingo.feast.Exchange;

import java.io.IOException;
import java.util.List;

/**
 * Created by rdiekema on 9/23/16.
 */
public class OutProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws IOException {
        List<Asset> assetList = exchange.getAssets();
        exchange = new Exchange();
        exchange.setAssets(assetList);
    }
}
