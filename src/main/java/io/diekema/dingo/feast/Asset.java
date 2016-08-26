package io.diekema.dingo.feast;

import java.nio.file.Path;

/**
 * Created by rdiekema on 8/23/16.
 */
public class Asset {
    private String name;
    private String content;
    private Path originalPath;
    private Path currentPath;

    public Asset(String name, String content, Path originalPath) {
        this.name = name;
        this.content = content;
        this.originalPath = originalPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Path getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(Path originalPath) {
        this.originalPath = originalPath;
    }

    public Path getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(Path currentPath) {
        this.currentPath = currentPath;
    }
}
