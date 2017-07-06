package io.diekema.dingo.feast.test

import io.diekema.dingo.feast.*
import io.diekema.dingo.feast.destinations.FileSystemDestination
import io.diekema.dingo.feast.processors.ConcatenatingProcessor
import io.diekema.dingo.feast.processors.js.JavascriptProcessor
import io.diekema.dingo.feast.processors.less.LessProcessor
import io.diekema.dingo.feast.processors.sass.ScssProcessor
import org.junit.Test
import java.io.IOException


/**
 * Created by rdiekema on 8/23/16.
 */
class PipelineTest {
    val f = DSL.Companion

    @Test
    @Throws(IOException::class)
    fun testNoOp() {
        val results = f.pipe().from(f.fileSystem("src/test/resources", "glob:{**/,}*.{less,js,html}"))
                .process(ConcatenatingProcessor())
                .`as`("noop_concat")
                .file("target/dist")
                .run()

        for (result in results) {
            println(result.content)
            println(result.name)
            println(result.currentPath.toString())
        }
    }

    @Test
    @Throws(IOException::class)
    fun testClosureCompiler() {
        val results = f.pipe().from(f.fileSystem("src/test/resources/js", "glob:{**/,}*.{js}"))
                .process(JavascriptProcessor())
                .`as`("app.min")
                .file("target/dist")
                .run()

        for (result in results) {
            println(result.content)
            println(result.name)
            println(result.currentPath.toString())
        }
    }

    @Test
    @Throws(IOException::class)
    fun testJsMinifyRename() {
        val results = f.pipe().from(f.fileSystem("src/test/resources", "glob:{**/,}*.{js}"))
                .process(JavascriptProcessor())
                .`as`("app.min")
                .file("target/dist")
                .run()


        for (result in results) {
            println(result.content)
            println(result.name)
            println(result.currentPath.toString())
        }
    }

    @Test
    @Throws(IOException::class)
    fun testHtmlOutput() {
        val results = f.pipe().from(f.fileSystem("src/test/resources", "glob:{**/,}*.{html}")).file("target/dist").run()


        for (result in results) {
            println(result.content)
            println(result.name)
            println(result.currentPath.toString())
        }
    }

    @Test
    @Throws(IOException::class)
    fun testHtmlReferenceReplace() {
        val results = f.pipe().from(f.fileSystem("src/test/resources", "glob:{**/,}*.{js}"))
                .process(JavascriptProcessor())
                .`as`("app.min.js")
                .file("target/dist")
                .replace(Pipeline().from(f.fileSystem("src/test/resources", "glob:{**/,}*.{html}")))
                .file("target/dist")
                .run()


        for (result in results) {
            println(result.content)
            println(result.name)
            println(result.currentPath.toString())
        }
    }

    @Test
    @Throws(IOException::class)
    fun testLessOutput() {
        val results = f.pipe().from(f.fileSystem("src/test/resources/test.less", "glob:{**/,}*.{less}")).process(LessProcessor()).`as`("less_test").to(FileSystemDestination("target/dist")).run()

        for (result in results) {
            println(result.content)
            println(result.name)
            println(result.currentPath.toString())
        }
    }

    @Test
    @Throws(IOException::class)
    fun testLessAndJsReplaceOutput() {
        val results = f.pipe()
                .enrich(
                        Pipeline().from(f.fileSystem("src/test/resources", "glob:{**/,}*.{js}"))
                                .process(JavascriptProcessor())
                                .`as`("app_scripts")
                                .file("target/dist")
                )
                .enrich(
                        Pipeline().from(f.fileSystem("src/test/resources", "glob:{**/,}*.{less}"))
                                .process(LessProcessor())
                                .`as`("app_styles")
                                .file("target/dist")
                )
                .replace(
                        Pipeline().from(f.fileSystem("src/test/resources", "glob:{**/,}*.{html}"))
                )
                .file("target/dist")
                .run()

        for (result in results) {
            println(result.content)
            println(result.name)
            println(result.currentPath.toString())
        }
    }

    @Test
    @Throws(IOException::class)
    fun testNewPiping() {
        f.pipe().from(arrayOf(
                f.pipe().from(f.fileSystem("src/test/resources", "glob:{**/,}*.{js}"))
                        .process(JavascriptProcessor())
                        .`as`("app_scripts")
                        .to(f.fileSystem("target/dist")),
                f.pipe().from(f.fileSystem("src/test/resources", "glob:{**/,}*.{less}"))
                        .process(LessProcessor())
                        .`as`("app_styles")
                        .to(f.fileSystem("target/dist")))
        ).replace(
                f.pipe().from(f.fileSystem("src/test/resources", "glob:{**/,}*.{html}"))
        ).to(f.fileSystem("target/dist")).log().run()
    }

    @Test
    @Throws(IOException::class)
    fun testTemplateCaching() {
        f.pipe()
                .from(f.fileSystem("src/test/resources/js/templates", "glob:{**/,}*.{html}"))
                .process(f.templateCache("exampleApp", "templates.cache"))
                .log()
                .to(f.fileSystem("target/dist/templates"))
                .run()
    }

    @Test
    @Throws(IOException::class)
    fun testSassOutput() {
        val results = f.pipe().from(f.singleFile("src/test/resources/sass/test.scss")).process(ScssProcessor()).`as`("sass_test").to(FileSystemDestination("target/dist")).run()

        for (result in results) {
            println(result.content)
            println(result.name)
            println(result.currentPath.toString())
        }
    }
}
