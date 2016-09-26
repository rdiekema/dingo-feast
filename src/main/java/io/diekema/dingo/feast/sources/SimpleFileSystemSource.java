/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.sources;

import io.diekema.dingo.feast.Asset;
import io.diekema.dingo.feast.Exchange;
import io.diekema.dingo.feast.Source;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by rdiekema on 9/25/16.
 */
public class SimpleFileSystemSource implements Source {

    private static final Logger log = LoggerFactory.getLogger(RecursiveFileSystemSource.class.getName());

    private String inputPath;
    private String inputPatternAndSyntax;

    public SimpleFileSystemSource(String filePath, String syntaxAndPattern) {
        this.inputPath = filePath;
        this.inputPatternAndSyntax = syntaxAndPattern;
    }

    @Override
    public Exchange collect() throws IOException {
        final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(inputPatternAndSyntax);
        final AtomicInteger fileCount = new AtomicInteger(0);
        final List<FilePayload> payloads = new LinkedList<>();

        Files.walkFileTree(FileSystems.getDefault().getPath(inputPath), Collections.<FileVisitOption>emptySet(), 1, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (pathMatcher.matches(file)) {
                    fileCount.incrementAndGet();
                    payloads.add(new FilePayload(file));
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });

        log.info(String.format("Found %d files.", fileCount.intValue()));

        List<Asset> assets = new LinkedList<>();
        for (FilePayload filePayload : payloads) {
            assets.add(new Asset(filePayload.getName(), IOUtils.toString(filePayload.getInputStream(), UTF_8), filePayload.getPath()));
        }

        Exchange exchange = new Exchange();
        exchange.enrich(assets);

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
