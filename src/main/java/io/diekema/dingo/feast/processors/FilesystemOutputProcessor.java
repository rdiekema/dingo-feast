package io.diekema.dingo.feast.processors;

import io.diekema.dingo.feast.Asset;
import io.diekema.dingo.feast.DSL;
import io.diekema.dingo.feast.Exchange;
import io.diekema.dingo.feast.Message;
import io.diekema.dingo.feast.processors.Processor;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by rdiekema on 8/23/16.
 */
public class FilesystemOutputProcessor implements Processor {

    String outputPath;

    public FilesystemOutputProcessor(String outputPath) {
        this.outputPath = outputPath;
    }

    @Override
    public void process(Exchange exchange) throws IOException {
        if (!StringUtils.isEmpty(outputPath)) {
            for (Asset asset : (List<Asset>) exchange.getAssets()) {
                Path absoluteOutPut = Paths.get(outputPath).toAbsolutePath();

                if(!Files.exists(absoluteOutPut)){
                    Files.createDirectory(absoluteOutPut);
                }

                String absolutePath = absoluteOutPut.toString() + File.separator + asset.getName();
                FileWriter fileWriter = new FileWriter(absolutePath);
                fileWriter.write(asset.getContent());
                fileWriter.flush();
                fileWriter.close();
                asset.setCurrentPath(Paths.get(absolutePath));
            }
        }
    }
}
