/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */

package io.diekema.dingo.feast.mojo;

import groovy.lang.GroovyShell;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by rdiekema on 8/26/16.
 */

@Mojo(name = "feast")
public class FeastMojo extends AbstractMojo {

    @Parameter
    private String scriptFile;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (scriptFile != null && Files.exists(Paths.get(scriptFile))) {
            GroovyShell groovyShell = new GroovyShell();
            try {
                groovyShell.evaluate(Paths.get(scriptFile).toFile());
            } catch (IOException e) {
                getLog().error(e.getMessage());
            }
        }

    }
}
