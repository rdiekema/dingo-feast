package io.diekema.dingo.feast.sources;

import io.diekema.dingo.feast.Asset;
import io.diekema.dingo.feast.Exchange;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by rdiekema on 11/23/16.
 */
public class SingleFileSource implements Source {
    private static final Logger log = LoggerFactory.getLogger(SingleFileSource.class.getName());
    String absolutePath;

    public SingleFileSource(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    @Override
    public Exchange collect() throws IOException {
        Path singleFilePath = Paths.get(absolutePath);
        FilePayload filePayload = new FilePayload(singleFilePath);
        log.info(String.format("Found %d files.", 1));
        Exchange exchange = new Exchange();
        exchange.enrich(Collections.singletonList(new Asset(filePayload.getName(), IOUtils.toString(filePayload.getInputStream(), UTF_8), filePayload.getPath())));

        return exchange;
    }

    static class FilePayload {
        String name;
        Path path;
        InputStream inputStream;

        public FilePayload(Path file) throws FileNotFoundException {
            this.path = file;
            this.name = file.getFileName().toString();
            this.inputStream = new FileInputStream(file.toFile());
        }

        public String getName() {
            return name;
        }

        public Path getPath() {
            return path;
        }

        public InputStream getInputStream() {
            return inputStream;
        }
    }
}
