/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast;

import io.diekema.dingo.feast.destinations.Destination;
import io.diekema.dingo.feast.destinations.FileSystemDestination;
import io.diekema.dingo.feast.processors.angularjs.TemplateCacheProcessor;
import io.diekema.dingo.feast.sources.RecursiveFileSystemSource;
import io.diekema.dingo.feast.sources.SimpleFileSystemSource;
import io.diekema.dingo.feast.sources.SingleFileSource;
import io.diekema.dingo.feast.sources.Source;

/**
 * Created by rdiekema on 9/23/16.
 */

public class DSL {
    public static Source fileSystem(String filePath, String syntaxAndPattern) {
        return new SimpleFileSystemSource(filePath, syntaxAndPattern);
    }

    public static Source fileSystem(String filePath, String syntaxAndPattern, boolean recursive){
        if(recursive){
            return new RecursiveFileSystemSource(filePath, syntaxAndPattern);
        }
        else{
            return fileSystem(filePath, syntaxAndPattern);
        }
    }

    public static Source singleFile(String filePath){
        return new SingleFileSource(filePath);
    }

    public static Destination fileSystem(String destination) {
        return new FileSystemDestination(destination);
    }

    public static Pipeline pipe() {
        return new Pipeline();
    }

    public static TemplateCacheProcessor templateCache(String appName, String outputFilename){
        return new TemplateCacheProcessor(appName, outputFilename);
    }
}
