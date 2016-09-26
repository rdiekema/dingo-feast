/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.processors;

import io.diekema.dingo.feast.Asset;
import io.diekema.dingo.feast.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by rdiekema on 9/23/16.
 */
public class LoggingProcessor implements Processor {
    private static final  Logger log = LoggerFactory.getLogger(LoggingProcessor.class.getName());

    @Override
    public void process(Exchange exchange) throws IOException {
        for(Asset asset : exchange.getAssets()){
            log.info(asset.toString());
        }
    }
}
