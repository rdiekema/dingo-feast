/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.processors;

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
 * Created by rdiekema on 8/23/16.
 */
public class FilesystemOutputProcessor implements Processor {

    private String outputPath;
    private String NAME_TEMPLATE = "%s%s.%s";


    public FilesystemOutputProcessor(String outputPath) {
        this.outputPath = outputPath;
    }

    @Override
    public void process(Exchange exchange) throws IOException {
        if (!StringUtils.isEmpty(outputPath)) {
            for (Asset asset : exchange.getAssets()) {
                Path absoluteOutPut = Paths.get(outputPath).toAbsolutePath();

                if (!Files.exists(absoluteOutPut)) {
                    Files.createDirectory(absoluteOutPut);
                }

                String outputFileName = String.format(NAME_TEMPLATE, asset.getName(), asset.getRevision()==null?"":asset.getRevision(), asset.getExtension());

                String absolutePath = absoluteOutPut.toString() + File.separator + outputFileName;
                FileWriter fileWriter = new FileWriter(absolutePath);
                fileWriter.write(asset.getContent());
                fileWriter.flush();
                fileWriter.close();
                asset.setCurrentPath(Paths.get(absolutePath));
            }
        }
    }
}
