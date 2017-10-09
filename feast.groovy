/*
 * Copyright (c) 2016. Richard Diekema - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Richard Diekema <rdiekema@gmail.com>, September 2016
 */


import io.diekema.dingo.feast.Asset

import static io.diekema.dingo.feast.DSL.*

def outputDir = "target/dist"
def inputDir = "src/test/resources"
def templateDir = inputDir + "/js/templates"

def jsPipe = pipeline()
        .from(files(inputDir, "glob:{**/,}*.{js}", true))
        .process(js())
        .alias("app_scripts")
        .process(version())
        .file(outputDir)

def lessPipe = pipeline()
        .from(files(inputDir + "/test.less", "glob:{**/,}*.{less}"))
        .process(less())
        .alias("app_styles")
        .process(version())
        .file(outputDir)

def scssPipe = pipeline()
        .from(singleFile(inputDir + "/sass/test.scss"))
        .process(sass(inputDir + "/nested_scss"))
        .alias("app_sass_styles")
        .process(version())
        .file(outputDir)

def spritePipe = pipeline()
        .from(files("src/test/resources/icons", "glob:{**/,}*.{png}"))
        .process(sprite(outputDir, "sprites", "sprites"))
        .alias("app_css_sprites")
        .process(version())
        .file(outputDir)

def templateCachePipe = pipeline().from(files(templateDir, "glob:{**/,}*.{html}"))
        .process(templateCache("app", "templates"))
        .process(js())
        .alias("templates")
        .process(version())
        .file(outputDir)

def htmlPipe = pipeline().from(files(inputDir, "glob:{**/,}*.{html}"));

def start = new Date()

List<Asset> results =
        pipeline()
                .from(jsPipe, lessPipe, scssPipe, templateCachePipe, spritePipe)
                .replace(htmlPipe)
                .file(outputDir)
                .run()

println "\nTotal Time:"
println new Date().getTime() - start.getTime() + "ms"


