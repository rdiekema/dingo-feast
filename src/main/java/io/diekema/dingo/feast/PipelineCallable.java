/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by rdiekema on 9/23/16.
 */
public class PipelineCallable implements Callable<List<Asset>> {

    private static final Logger log = LoggerFactory.getLogger(PipelineCallable.class.getName());

    private Pipeline pipeline;
    private List<Asset> assets = new ArrayList<>();

    public PipelineCallable(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    @Override
    public List<Asset> call() throws Exception {
        try {
            assets.addAll(pipeline.run());
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return assets;
    }
}
