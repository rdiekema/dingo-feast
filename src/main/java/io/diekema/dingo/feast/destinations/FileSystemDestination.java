package io.diekema.dingo.feast.destinations;

import io.diekema.dingo.feast.Asset;
import io.diekema.dingo.feast.Exchange;
import io.diekema.dingo.feast.Features;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by rdiekema on 9/23/16.
 */
public class FileSystemDestination implements Destination {

    String outputPath;

    public FileSystemDestination(String outputPath) {
        this.outputPath = outputPath;
    }

    @Override
    public boolean deliver(Exchange exchange) throws IOException {
        if (!StringUtils.isEmpty(outputPath)) {

            for (Asset asset : exchange.getAssets()) {
                Path absoluteOutPut = Paths.get(outputPath).toAbsolutePath();

                if (!Files.exists(absoluteOutPut)) {
                    Files.createDirectories(absoluteOutPut);
                }

                String absolutePath = absoluteOutPut.toString() + File.separator + asset.getName() + Features.DOT + asset.getExtension();
                FileWriter fileWriter = new FileWriter(absolutePath);
                fileWriter.write(asset.getContent());
                fileWriter.flush();
                fileWriter.close();
                asset.setCurrentPath(Paths.get(absolutePath));
            }
        }
        return false;
    }
}
