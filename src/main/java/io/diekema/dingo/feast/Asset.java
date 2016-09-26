/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

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
        this.content = content;
        this.originalPath = originalPath != null ? originalPath.toAbsolutePath() : null;

        if(name != null && name.contains(".")){
            String[] nameWithExtension = name.split("\\.");

            if(nameWithExtension.length > 1){
                extension = nameWithExtension[nameWithExtension.length - 1];
                this.name = name.substring(0, name.indexOf(extension)-1);
            }
        }
        else{
            this.name = name;
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

    @Override
    public String toString() {
        return "Asset{" +
                "name='" + name + "'" +
                ", originalPath='" + originalPath + "'" +
                ", currentPath='" + currentPath + "'" +
                ", extension='" + extension + "'" +
                '}';
    }
}
