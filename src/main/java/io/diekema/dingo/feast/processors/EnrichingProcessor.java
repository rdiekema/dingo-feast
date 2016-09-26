/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.processors;

import io.diekema.dingo.feast.Exchange;
import io.diekema.dingo.feast.Pipeline;

import java.io.IOException;

/**
 * Created by rdiekema on 9/9/16.
 */
public class EnrichingProcessor extends NoOpMessageProcessor {

    private Pipeline pipeline;

    public EnrichingProcessor(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public void process(Exchange exchange) throws IOException {
        exchange.enrich(pipeline.run());
    }
}
