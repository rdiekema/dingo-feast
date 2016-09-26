/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

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
