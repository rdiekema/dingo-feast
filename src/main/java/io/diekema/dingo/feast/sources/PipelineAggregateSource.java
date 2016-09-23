package io.diekema.dingo.feast.sources;

import io.diekema.dingo.feast.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by rdiekema on 9/23/16.
 */
public class PipelineAggregateSource implements Source {

    private static final Logger log = LoggerFactory.getLogger(PipelineAggregateSource.class.getName());

    private Pipeline[] sourcePipelines;
    private final CompletionService<List<Asset>> completionService;

    public PipelineAggregateSource(Pipeline... sourcePipelines) {
        this.sourcePipelines = sourcePipelines;
        this.completionService = new ExecutorCompletionService<>(Executors.newFixedThreadPool(sourcePipelines.length));
    }

    @Override
    public Exchange collect() throws IOException {

        List<Future<List<Asset>>> submitted = new ArrayList<>();
        for (Pipeline p : sourcePipelines) {
            submitted.add(completionService.submit(new PipelineCallable(p)));
        }

        List<Asset> assetList = new ArrayList<>();
        for(int i = 0; i < submitted.size(); i++){
            try {
                final Future<List<Asset>> result = completionService.take();
                assetList.addAll(result.get());
            } catch (InterruptedException | ExecutionException e) {
                log.error(e.getMessage());
            }
        }

        Exchange exchange = new Exchange();
        exchange.setAssets(assetList);
        return exchange;
    }
}
