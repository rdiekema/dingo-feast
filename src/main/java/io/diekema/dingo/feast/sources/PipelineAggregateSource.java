package io.diekema.dingo.feast.sources;

import io.diekema.dingo.feast.Asset;
import io.diekema.dingo.feast.Exchange;
import io.diekema.dingo.feast.Pipeline;
import io.diekema.dingo.feast.Source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rdiekema on 9/23/16.
 */
public class PipelineAggregateSource implements Source {

    Pipeline[] sourcePipelines;

    public PipelineAggregateSource(Pipeline... sourcePipelines) {
        this.sourcePipelines = sourcePipelines;
    }

    @Override
    public Exchange collect() throws IOException {
        Exchange exchange = new Exchange();
        List<Asset> assetList = new ArrayList<>();
        for(Pipeline p : sourcePipelines){
            assetList.addAll(p.run());
        }
        exchange.setAssets(assetList);
        return exchange;
    }
}
