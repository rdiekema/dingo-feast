package io.diekema.dingo.feast.processors;

import io.diekema.dingo.feast.Exchange;

import java.io.IOException;

/**
 * Created by rdiekema on 8/23/16.
 */
public interface Processor {

    public void process(Exchange exchange) throws IOException;

}
