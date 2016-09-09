package io.diekema.dingo.feast.processors;

import io.diekema.dingo.feast.Asset;
import io.diekema.dingo.feast.Exchange;
import io.diekema.dingo.feast.Pipeline;

import java.io.IOException;
import java.util.List;

/**
 * Created by rdiekema on 9/9/16.
 */
public class EnrichingProcessor extends AbstractMessageProcessor {

    private Pipeline pipeline;

    public EnrichingProcessor(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public void process(Exchange exchange) throws IOException {
        exchange.enrich(pipeline.run());
    }
}
