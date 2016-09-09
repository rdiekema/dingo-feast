package io.diekema.dingo.feast.processors.html;

import io.diekema.dingo.feast.Exchange;
import io.diekema.dingo.feast.Pipeline;
import io.diekema.dingo.feast.processors.Processor;

import java.io.IOException;

/**
 * Created by rdiekema on 8/26/16.
 */
public class ReplaceAssetProcessor implements Processor {

    private String target;
    private Pipeline enrichingPipeline;

    public ReplaceAssetProcessor(String target, Pipeline enrichingPipeline) {
        this.target = target;
        this.enrichingPipeline = enrichingPipeline;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Pipeline getEnrichingPipeline() {
        return enrichingPipeline;
    }

    public void setEnrichingPipeline(Pipeline enrichingPipeline) {
        this.enrichingPipeline = enrichingPipeline;
    }

    @Override
    public void process(Exchange exchange) throws IOException {

    }
}
