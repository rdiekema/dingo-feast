/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast;

import io.diekema.dingo.feast.destinations.Destination;
import io.diekema.dingo.feast.processors.*;
import io.diekema.dingo.feast.processors.html.HtmlReplaceAssetProcessor;
import io.diekema.dingo.feast.sources.PipelineAggregateSource;
import io.diekema.dingo.feast.sources.PipelineSource;
import io.diekema.dingo.feast.sources.Source;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by rdiekema on 8/23/16.
 */
public class Pipeline {

    private static final Logger log = Logger.getLogger(Pipeline.class.getName());

    private Source source;
    private LinkedList<Processor> joints = new LinkedList<>();
    private Destination destination;

    public Pipeline() {
    }

    public Pipeline from(Pipeline pipeline) {
        this.source = new PipelineSource(pipeline);
        return this;
    }

    public Pipeline from(Source source) {
        this.source = source;
        return this;
    }

    public Pipeline from(Pipeline... pipelines) {
        this.source = new PipelineAggregateSource(pipelines);
        return this;
    }

    public void to(Pipeline... pipeline) {
        for (Pipeline p : pipeline) {
            p.from(new PipelineSource(this));
        }
    }

    public Pipeline to(Destination destination) {
        this.destination = destination;
        return this;
    }

    public Pipeline as(String name) {
        joints.add(new RenamingProcessor(name));
        return this;
    }

    public Pipeline replace(Pipeline from) {
        joints.add(new HtmlReplaceAssetProcessor(from));
        return this;
    }

    public Pipeline file(String outputPath) {
        joints.add(new FilesystemOutputProcessor(outputPath));
        return this;
    }

    public Pipeline enrich(Pipeline pipeline) {
        joints.add(new EnrichingProcessor(pipeline));
        return this;
    }

    public Pipeline process(Processor processor) {
        joints.add(processor);
        return this;
    }

    public Pipeline log() {
        joints.add(new LoggingProcessor());
        return this;
    }

    public List<Asset> run() throws IOException {
        Exchange exchange;
        if (source != null) {
            exchange = source.collect();
        } else {
            exchange = new Exchange();
        }

        for (Processor processor : joints) {
            processor.process(exchange);
        }

        if (destination != null) {
            destination.deliver(exchange);
        }

        return exchange.getAssets();
    }

}
