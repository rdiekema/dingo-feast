/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */


import io.diekema.dingo.feast.Asset
import io.diekema.dingo.feast.Exchange
import io.diekema.dingo.feast.processors.Processor
import org.slf4j.LoggerFactory

import static io.diekema.dingo.feast.DSL.*

def outputDir = "target/dist"
def inputDir = "src/test/resources"
def templateDir = inputDir + "/js/templates"

def jsPipe = pipeline().from(fileSystem(inputDir, "glob:{**/,}*.{js}", true)).process(js()).as("app_scripts").process(version()).file(outputDir)
def lessPipe = pipeline().from(fileSystem(inputDir + "/test.less", "glob:{**/,}*.{less}")).process(less()).as("app_styles").process(version()).file(outputDir)
def scssPipe = pipeline().from(singleFile(inputDir + "/sass/test.scss")).process(sass(inputDir + "/nested_scss")).as("app_sass_styles").process(version()).file(outputDir)
def spritePipe = pipeline().from(fileSystem("src/test/resources/icons", "glob:{**/,}*.{png}")).process(sprite(outputDir, "sprites", "sprites")).as("app_css_sprites").process(version()).file(outputDir)

def templateCachePipe = pipeline().from(fileSystem(templateDir, "glob:{**/,}*.{html}"))
        .process(templateCache("app", "templates"))
        .process(js())
        .as("templates")
        .process(version())
        .file(outputDir)

def htmlPipe = pipeline().from(fileSystem(inputDir, "glob:{**/,}*.{html}"));

def start = new Date()

List<Asset> results =
        pipeline()
                .from(jsPipe, lessPipe, scssPipe, templateCachePipe, spritePipe)
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


