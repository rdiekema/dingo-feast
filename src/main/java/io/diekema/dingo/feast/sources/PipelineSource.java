package io.diekema.dingo.feast.sources;

import io.diekema.dingo.feast.Exchange;
import io.diekema.dingo.feast.Pipeline;
import io.diekema.dingo.feast.Source;

import java.io.IOException;

/**
 * Created by rdiekema on 9/23/16.
 */
public class PipelineSource implements Source {

    private Pipeline source;

    public PipelineSource(Pipeline source) {
        this.source = source;
    }

    @Override
    public Exchange collect() throws IOException {
        Exchange exchange = new Exchange();
        exchange.setAssets(source.run());
        return exchange;
    }
}
