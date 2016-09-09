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
    private String extension;

    public Asset(String name, String content, Path originalPath, String extension){
        this.name = name;
        this.content = content;
        this.originalPath = originalPath;
        this.extension = extension;
    }

    public Asset(String name, String content, Path originalPath) {
        this.name = name;
        this.content = content;
        this.originalPath = originalPath;

        if(name.contains(".")){
            String[] nameWithExtension = name.split("\\.");

            if(nameWithExtension.length > 1){
                extension = nameWithExtension[1];
            }
        }
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

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
