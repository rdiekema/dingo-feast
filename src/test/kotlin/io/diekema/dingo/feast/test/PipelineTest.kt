package io.diekema.dingo.feast.test

import io.diekema.dingo.feast.DSL
import io.diekema.dingo.feast.Pipeline
import io.diekema.dingo.feast.destinations.FileSystemDestination
import io.diekema.dingo.feast.processors.ConcatenatingProcessor
import io.diekema.dingo.feast.processors.image.PngProcessor
import io.diekema.dingo.feast.processors.js.JavascriptProcessor
import io.diekema.dingo.feast.processors.less.LessProcessor
import io.diekema.dingo.feast.processors.sass.ScssProcessor
import kotlinx.coroutines.experimental.runBlocking
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
        runBlocking {
            val results = f.pipeline().from(f.files("src/test/resources", "glob:{**/,}*.{less,js,html}"))
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
    }

    @Test
    @Throws(IOException::class)
    fun testClosureCompiler() {
        runBlocking {
            val results = f.pipeline().from(f.files("src/test/resources/js", "glob:{**/,}*.{js}"))
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
    }

    @Test
    @Throws(IOException::class)
    fun testJsMinifyRename() {
        runBlocking {
            val results = f.pipeline().from(f.files("src/test/resources", "glob:{**/,}*.{js}"))
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
    }

    @Test
    @Throws(IOException::class)
    fun testHtmlOutput() {
        runBlocking {
            val results = f.pipeline().from(f.files("src/test/resources", "glob:{**/,}*.{html}")).file("target/dist").run()


            for (result in results) {
                println(result.content)
                println(result.name)
                println(result.currentPath.toString())
            }
        }
    }

    @Test
    @Throws(IOException::class)
    fun testHtmlReferenceReplace() {
        runBlocking {
            val results = f.pipeline().from(f.files("src/test/resources", "glob:{**/,}*.{js}"))
                    .process(JavascriptProcessor())
                    .`as`("app.min.js")
                    .file("target/dist")
                    .replace(Pipeline().from(f.files("src/test/resources", "glob:{**/,}*.{html}")))
                    .file("target/dist")
                    .run()


            for (result in results) {
                println(result.content)
                println(result.name)
                println(result.currentPath.toString())
            }
        }
    }

    @Test
    @Throws(IOException::class)
    fun testLessOutput() {
        runBlocking {
            val results = f.pipeline().from(f.files("src/test/resources/test.less", "glob:{**/,}*.{less}")).process(LessProcessor()).`as`("less_test").to(FileSystemDestination("target/dist")).run()

            for (result in results) {
                println(result.content)
                println(result.name)
                println(result.currentPath.toString())
            }
        }
    }

    @Test
    @Throws(IOException::class)
    fun testLessAndJsReplaceOutput() {
        runBlocking {
            val results = f.pipeline()
                    .enrich(
                            Pipeline().from(f.files("src/test/resources", "glob:{**/,}*.{js}"))
                                    .process(JavascriptProcessor())
                                    .`as`("app_scripts")
                                    .file("target/dist")
                    )
                    .enrich(
                            Pipeline().from(f.files("src/test/resources", "glob:{**/,}*.{less}"))
                                    .process(LessProcessor())
                                    .`as`("app_styles")
                                    .file("target/dist")
                    )
                    .replace(
                            Pipeline().from(f.files("src/test/resources", "glob:{**/,}*.{html}"))
                    )
                    .file("target/dist")
                    .run()

            for (result in results) {
                println(result.content)
                println(result.name)
                println(result.currentPath.toString())
            }
        }
    }

    @Test
    @Throws(IOException::class)
    fun testNewPiping() {
        runBlocking {
            f.pipeline().from(arrayOf(
                    f.pipeline().from(f.files("src/test/resources", "glob:{**/,}*.{js}"))
                            .process(JavascriptProcessor())
                            .`as`("app_scripts")
                            .to(f.files("target/dist")),
                    f.pipeline().from(f.files("src/test/resources", "glob:{**/,}*.{less}"))
                            .process(LessProcessor())
                            .`as`("app_styles")
                            .to(f.files("target/dist")))
            ).replace(
                    f.pipeline().from(f.files("src/test/resources", "glob:{**/,}*.{html}"))
            ).to(f.files("target/dist")).log().run()
        }
    }

    @Test
    @Throws(IOException::class)
    fun testTemplateCaching() {
        runBlocking {
            f.pipeline()
                    .from(f.files("src/test/resources/js/templates", "glob:{**/,}*.{html}"))
                    .process(f.templateCache("exampleApp", "templates.cache"))
                    .log()
                    .to(f.files("target/dist/templates"))
                    .run()
        }
    }

    @Test
    @Throws(IOException::class)
    fun testSassOutput() {
        runBlocking {
            val results = f.pipeline().from(f.singleFile("src/test/resources/sass/test.scss")).process(ScssProcessor()).`as`("sass_test").to(FileSystemDestination("target/dist")).run()

            for (result in results) {
                println(result.content)
                println(result.name)
                println(result.currentPath.toString())
            }
        }
    }

    @Test
    @Throws(IOException::class)
    fun testPngSpriting() {
        runBlocking {
            val result = f.pipeline().from(f.files("src/test/resources/icons", "glob:{**/,}*.{png}")).process(PngProcessor("target/dist", "sprites", filename = "sprites")).run()
        }
    }
}
