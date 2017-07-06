/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */


import io.diekema.dingo.feast.Asset

import io.diekema.dingo.feast.Exchange
import io.diekema.dingo.feast.processors.Processor
import io.diekema.dingo.feast.processors.VersionProcessor
import io.diekema.dingo.feast.processors.js.JavascriptProcessor
import io.diekema.dingo.feast.processors.less.LessProcessor
import io.diekema.dingo.feast.processors.sass.ScssProcessor
import org.slf4j.LoggerFactory

def outputDir = "target/dist"
def inputDir = "src/test/resources"
def templateDir = inputDir + "/js/templates"

def jsPipe = pipe().from(fileSystem(inputDir, "glob:{**/,}*.{js}", true)).process(new JavascriptProcessor()).as("app_scripts").process(new VersionProcessor()).file(outputDir)
def lessPipe = pipe().from(fileSystem(inputDir + "/test.less", "glob:{**/,}*.{less}")).process(new LessProcessor()).as("app_styles").process(new VersionProcessor()).file(outputDir)
def scssPipe = pipe().from(DSL.Companion.singleFile(inputDir + "/sass/test.scss")).process(new ScssProcessor(inputDir + "/nested_scss")).as("app_sass_styles").process(new VersionProcessor()).file(outputDir)

def templateCachePipe = pipe().from(fileSystem(templateDir, "glob:{**/,}*.{html}"))
        .process(templateCache("app", "templates"))
        .process(new JavascriptProcessor())
        .as("templates")
        .process(new VersionProcessor() as Processor)
        .file(outputDir)

def htmlPipe = pipe().from(fileSystem(inputDir, "glob:{**/,}*.{html}"));

def start = new Date()

List<Asset> results =
        pipe()
                .from(jsPipe, lessPipe, scssPipe, templateCachePipe)
                .replace(htmlPipe)
                .file(outputDir)
                .run()

println "\nTotal Time:"
println new Date().getTime() - start.getTime() + "ms"

// Test class to demonstrate implementing additional processors from within a groovy source file.
class LoggingGroovyClass implements Processor {
    def log = LoggerFactory.getLogger(LoggingGroovyClass.class.getName());

    @Override
    void process(Exchange exchange) throws IOException {
        log.info(exchange.getAssets().get(0).getName())
    }
}


