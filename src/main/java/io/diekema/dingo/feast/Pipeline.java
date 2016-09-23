/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast;

import io.diekema.dingo.feast.processors.*;
import io.diekema.dingo.feast.processors.html.HtmlReplaceAssetProcessor;
import io.diekema.dingo.feast.processors.js.JavascriptProcessor;
import io.diekema.dingo.feast.processors.less.LessProcessor;
import io.diekema.dingo.feast.sources.PipelineAggregateSource;
import io.diekema.dingo.feast.sources.PipelineSource;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static io.diekema.dingo.feast.Features.DOT;

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

    public Pipeline flow(String operation) {
        joints.add(SupportedOperations.get(operation));
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

    private static final class SupportedOperations {

        private static final Map<String, Processor> operations = new HashMap<String, Processor>();

        static {
            // Text Operations (Mostly for testing.
            operations.put(Features.Format.text + DOT + Features.Operations.concat, new ConcatenatingProcessor());

            // HTML Operations
            operations.put(Features.Format.html + DOT + Features.Operations.noop, new NoOpMessageProcessor());

            // CSS Operations
            operations.put(Features.Format.css + DOT + Features.Operations.concat, new ConcatenatingProcessor());

            // JS Operations
            operations.put(Features.Format.js + DOT + Features.Operations.concat, new ConcatenatingProcessor());
            operations.put(Features.Format.js + DOT + Features.Operations.minify, new JavascriptProcessor());

            // SASS Operations
            operations.put(Features.Format.sass + DOT + Features.Operations.concat, new ConcatenatingProcessor());

            // LESS Operations
            operations.put(Features.Format.less + DOT + Features.Operations.concat, new ConcatenatingProcessor());
            operations.put(Features.Format.less + DOT + Features.Operations.compile, new LessProcessor());
        }

        static Processor get(String operation) {
            return operations.get(operation);
        }
    }
}
