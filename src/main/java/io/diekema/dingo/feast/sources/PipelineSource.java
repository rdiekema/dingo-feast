/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

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
