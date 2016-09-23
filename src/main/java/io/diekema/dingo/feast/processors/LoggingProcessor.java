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
