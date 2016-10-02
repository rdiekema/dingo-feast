/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */


import io.diekema.dingo.feast.Asset
import io.diekema.dingo.feast.Exchange
import io.diekema.dingo.feast.processors.*
import org.slf4j.LoggerFactory

import static io.diekema.dingo.feast.DSL.*

def outputDir = "target/dist"
def inputDir = "src/test/resources"
def templateDir = inputDir + "/js/templates"

def jsPipe = pipe().from(fileSystem(inputDir, "glob:{**/,}*.{js}", true)).flow("js.min").as("app_scripts").process(new Version() as Processor).file(outputDir)
def lessPipe = pipe().from(fileSystem(inputDir, "glob:{**/,}*.{less}")).flow("less.compile").as("app_styles").process(new Version() as Processor).file(outputDir)

def templateCachePipe = pipe().from(fileSystem(templateDir, "glob:{**/,}*.{html}"))
        .process(templateCache("app", "templates"))
        .flow("js.min")
        .as("templates")
        .process(new Version() as Processor)
        .file(outputDir)

def htmlPipe = pipe().from(fileSystem(inputDir, "glob:{**/,}*.{html}"));

def start = new Date()

List<Asset> results =
        pipe()
                .from(jsPipe, lessPipe, templateCachePipe)
                .replace(htmlPipe)
                .file(outputDir)
                .run()

//for (Asset result in results) {
//    print(result.getContent())
//}

println "\nTotal Time:"
println new Date().getTime() - start.getTime() + "ms"

// Test class to demonstrate implementing additional processors from within a groovy source file.
public class LoggingGroovyClass implements Processor {
    def log = LoggerFactory.getLogger(LoggingGroovyClass.class.getName());

    @Override
    void process(Exchange exchange) throws IOException {
        log.info(exchange.getAssets().get(0).getName())
    }
}


