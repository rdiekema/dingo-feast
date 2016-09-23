package io.diekema.dingo.feast;

import io.diekema.dingo.feast.endpoints.FileSystemDestination;
import io.diekema.dingo.feast.sources.FileSystemSource;

/**
 * Created by rdiekema on 9/23/16.
 */

public class DSL {
    public static Source fileSystem(String filePath, String syntaxAndPattern) {
        return new FileSystemSource(filePath, syntaxAndPattern);
    }

    public static Destination fileSystem(String destination) {
        return new FileSystemDestination(destination);
    }

    public static Pipeline pipe() {
        return new Pipeline();
    }
}
