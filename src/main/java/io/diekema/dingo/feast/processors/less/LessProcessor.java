/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.processors.less;

import com.github.sommeri.less4j.Less4jException;
import com.github.sommeri.less4j.LessCompiler;
import com.github.sommeri.less4j.core.ThreadUnsafeLessCompiler;
import io.diekema.dingo.feast.Asset;
import io.diekema.dingo.feast.Exchange;
import io.diekema.dingo.feast.processors.NoOpMessageProcessor;
import io.diekema.dingo.feast.processors.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by rdiekema on 9/9/16.
 */
public class LessProcessor implements Processor {

    private static final Logger log = LoggerFactory.getLogger(LessProcessor.class.getName());

    @Override
    public void process(Exchange exchange) throws IOException {

        for (Asset asset : exchange.getAssets()) {
            try {
                LessCompiler compiler = new ThreadUnsafeLessCompiler();
                LessCompiler.CompilationResult result = compiler.compile(asset.getOriginalPath().toFile());
                exchange.setAssets(Arrays.asList(new Asset(asset.getName(), result.getCss(), null, "css"), new Asset(asset.getName(), result.getSourceMap(), null, "map")));
            } catch (Less4jException e) {
                log.error(e.getMessage());
            }
        }
    }
}
