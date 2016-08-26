package io.diekema.dingo.feast.processors;

import io.diekema.dingo.feast.Asset;
import io.diekema.dingo.feast.Exchange;
import io.diekema.dingo.feast.Message;

import java.io.IOException;
import java.util.List;


/**
 * Created by rdiekema on 8/23/16.
 */
public class AbstractMessageProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws IOException {
        if(exchange.getAssets() != null){
            return;
        }
    }
}
