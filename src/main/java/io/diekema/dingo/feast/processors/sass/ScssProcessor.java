package io.diekema.dingo.feast.processors.sass;

import com.cathive.sass.*;
import io.diekema.dingo.feast.Asset;
import io.diekema.dingo.feast.Exchange;
import io.diekema.dingo.feast.processors.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 * Created by rdiekema on 11/23/16.
 */
public class ScssProcessor implements Processor {
    private Logger log = LoggerFactory.getLogger(ScssProcessor.class.getName());
    private String[] includeDirs;

    public ScssProcessor(String... includeDirs) {
        this.includeDirs = includeDirs;
    }

    @Override
    public void process(Exchange exchange) throws IOException {
        List<Asset> assets = exchange.getAssets();

        Asset rootSource = assets.get(0);

        // Creates a new sass file context.
        SassContext ctx = SassFileContext.create(rootSource.getOriginalPath());
        SassOptions options = ctx.getOptions();

        options.pushIncludePath(rootSource.getOriginalPath().getParent());

        for (String includeDir : includeDirs) {
            options.pushIncludePath(Paths.get(includeDir));
        }

        options.setOutputStyle(SassOutputStyle.NESTED);
        options.setIsIndentedSyntaxSrc(rootSource.getExtension().equals("sass"));
        // any other options supported by libsass including source map stuff can be configured
        // as well here.

        // Will print the compiled CSS contents to the console. Use a FileOutputStream
        // or some other fancy mechanism to redirect the output to wherever you want.
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ctx.compile(byteArrayOutputStream);
            String compiledContent = new String(byteArrayOutputStream.toByteArray());

            exchange.setAssets(Collections.singletonList(new Asset(null, compiledContent, null, null, "css", null)));
        } catch (SassCompilationException e) {
            // Will print the error code, filename, line, column and the message provided
            // by libsass to the standard error output.
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(String.format("Compilation failed: %s", e.getMessage()));
        }
    }

}
