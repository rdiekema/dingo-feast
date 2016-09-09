package io.diekema.dingo.feast.processors.less;

import com.github.sommeri.less4j.Less4jException;
import com.github.sommeri.less4j.LessCompiler;
import com.github.sommeri.less4j.core.ThreadUnsafeLessCompiler;
import io.diekema.dingo.feast.Asset;
import io.diekema.dingo.feast.Exchange;
import io.diekema.dingo.feast.processors.AbstractMessageProcessor;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by rdiekema on 9/9/16.
 */
public class LessProcessor extends AbstractMessageProcessor {


    @Override
    public void process(Exchange exchange) throws IOException {
        StringBuilder lessConcatenator = new StringBuilder();

        for (Asset asset : exchange.getAssets()) {
            lessConcatenator.append(asset.getContent());
        }

        LessCompiler compiler = new ThreadUnsafeLessCompiler();

        try {
            LessCompiler.CompilationResult result = compiler.compile(lessConcatenator.toString());
            exchange.setAssets(Arrays.asList(new Asset("app", result.getCss(), null, "css"), new Asset("app", result.getSourceMap(), null, "map")));
        } catch (Less4jException e) {
            e.printStackTrace(System.out);
        }
    }
}
