/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.processors;

import io.diekema.dingo.feast.Asset;
import io.diekema.dingo.feast.Exchange;

import java.util.*;

/**
 * Created by rdiekema on 8/23/16.
 */
public class ConcatenatingProcessor extends NoOpMessageProcessor {
    @Override
    public void process(Exchange exchange) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Asset asset : exchange.getAssets()) {
            stringBuilder.append(asset.getContent()).append("\n");
        }

        exchange.setAssets(Collections.singletonList(new Asset(null, stringBuilder.toString(), null, exchange.getAssets().get(0).getExtension())));
    }
}
