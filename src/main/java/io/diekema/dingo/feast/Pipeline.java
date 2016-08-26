package io.diekema.dingo.feast;

import io.diekema.dingo.feast.processors.*;
import io.diekema.dingo.feast.processors.html.HtmlReplaceAssetProcessor;
import io.diekema.dingo.feast.processors.js.JavascriptProcessor;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static io.diekema.dingo.feast.DSL.DOT;

/**
 * Created by rdiekema on 8/23/16.
 */
public class Pipeline {

    private LinkedList<Processor> joint = new LinkedList<>();

    public Pipeline() {
    }

    public Pipeline from(String filePath, String syntaxAndPattern) {
        joint.add(new FileSystemInputProcessor(filePath, syntaxAndPattern));
        return this;
    }

    public Pipeline pipe(String operation) {
        joint.add(SupportedOperations.get(operation));
        return this;
    }

    public Pipeline as(String name){
        joint.add(new RenamingProcessor(name));
        return this;
    }

    public Pipeline replace(Pipeline from, String target){
        joint.add(new HtmlReplaceAssetProcessor(from, target));
        return this;
    }

    public Pipeline to(String outputPath) {
        joint.add(new FilesystemOutputProcessor(outputPath));
        return this;
    }

    public List run() throws IOException {
        Exchange exchange = new Exchange();

        for (Processor processor : joint) {
            processor.process(exchange);
        }

        return exchange.getAssets();
    }

    private static final class SupportedOperations {

        private static final Map<String, Processor> operations = new HashMap<String, Processor>();

        static {
            // Text Operations (Mostly for testing.
            operations.put(DSL.Format.text + DOT + DSL.Operations.concat, new ConcatenatingProcessor());

            // HTML Operations
            operations.put(DSL.Format.html + DOT + DSL.Operations.noop, new AbstractMessageProcessor());

            // CSS Operations
            operations.put(DSL.Format.css + DOT + DSL.Operations.concat, new ConcatenatingProcessor());

            // JS Operations
            operations.put(DSL.Format.js + DOT + DSL.Operations.concat, new ConcatenatingProcessor());
            operations.put(DSL.Format.js + DOT + DSL.Operations.minify, new JavascriptProcessor());

            // SASS Operations
            operations.put(DSL.Format.sass + DOT + DSL.Operations.concat, new ConcatenatingProcessor());

            // LESS Operations
            operations.put(DSL.Format.less + DOT + DSL.Operations.concat, new ConcatenatingProcessor());
        }

        static Processor get(String operation) {
            return operations.get(operation);
        }
    }
}
