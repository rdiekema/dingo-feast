package io.diekema.dingo.feast.processors;

import io.diekema.dingo.feast.*;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by rdiekema on 8/24/16.
 */
public class FileSystemInputProcessor implements Processor {

    private String inputPath;
    private String inputPatternAndSyntax;

    public FileSystemInputProcessor(String inputPath, String inputPatternAndSyntax) {
        this.inputPath = inputPath;
        this.inputPatternAndSyntax = inputPatternAndSyntax;
    }

    @Override
    public void process(Exchange exchange) throws IOException {
        final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(inputPatternAndSyntax);
        final AtomicInteger fileCount = new AtomicInteger(0);
        final List<FilePayload> payloads = new LinkedList<>();

        Files.walkFileTree(FileSystems.getDefault().getPath(inputPath), new SimpleFileVisitor<Path>() {
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

        System.out.println(String.format("Found %d files.", fileCount.intValue()));

        List<Asset> assets = new LinkedList<>();
        for (FilePayload filePayload : payloads) {
            assets.add(new Asset(filePayload.getName(), IOUtils.toString(filePayload.getInputStream(), UTF_8), filePayload.getPath()));
        }

        exchange.enrich(assets);
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
