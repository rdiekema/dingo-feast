package io.diekema.dingo.feast.test;

import io.diekema.dingo.feast.Asset;
import io.diekema.dingo.feast.Pipeline;
import io.diekema.dingo.feast.destinations.FileSystemDestination;
import io.diekema.dingo.feast.processors.ConcatenatingProcessor;
import io.diekema.dingo.feast.processors.js.JavascriptProcessor;
import io.diekema.dingo.feast.processors.less.LessProcessor;
import io.diekema.dingo.feast.processors.sass.ScssProcessor;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static io.diekema.dingo.feast.DSL.*;


/**
 * Created by rdiekema on 8/23/16.
 */
public class PipelineTest {

    @Test
    public void testNoOp() throws IOException {
        List<Asset> results = pipe().from(fileSystem("src/test/resources", "glob:{**/,}*.{less,js,html}"))
                .process(new ConcatenatingProcessor())
                .as("noop_concat")
                .file("target/dist")
                .run();

        for (Asset result : results) {
            System.out.println(result.getContent());
            System.out.println(result.getName());
            System.out.println(result.getCurrentPath().toString());
        }
    }

    @Test
    public void testClosureCompiler() throws IOException {
        List<Asset> results = pipe().from(fileSystem("src/test/resources/js", "glob:{**/,}*.{js}"))
                .process(new JavascriptProcessor())
                .as("app.min")
                .file("target/dist")
                .run();

        for (Asset result : results) {
            System.out.println(result.getContent());
            System.out.println(result.getName());
            System.out.println(result.getCurrentPath().toString());
        }
    }

    @Test
    public void testJsMinifyRename() throws IOException {
        List<Asset> results = pipe().from(fileSystem("src/test/resources", "glob:{**/,}*.{js}"))
                .process(new JavascriptProcessor())
                .as("app.min")
                .file("target/dist")
                .run();


        for (Asset result : results) {
            System.out.println(result.getContent());
            System.out.println(result.getName());
            System.out.println(result.getCurrentPath().toString());
        }
    }

    @Test
    public void testHtmlOutput() throws IOException {
        List<Asset> results = pipe().from(fileSystem("src/test/resources", "glob:{**/,}*.{html}")).file("target/dist").run();


        for (Asset result : results) {
            System.out.println(result.getContent());
            System.out.println(result.getName());
            System.out.println(result.getCurrentPath().toString());
        }
    }

    @Test
    public void testHtmlReferenceReplace() throws IOException {
        List<Asset> results = pipe().from(fileSystem("src/test/resources", "glob:{**/,}*.{js}"))
                .process(new JavascriptProcessor())
                .as("app.min.js")
                .file("target/dist")
                .replace(new Pipeline().from(fileSystem("src/test/resources", "glob:{**/,}*.{html}")))
                .file("target/dist")
                .run();


        for (Asset result : results) {
            System.out.println(result.getContent());
            System.out.println(result.getName());
            System.out.println(result.getCurrentPath().toString());
        }
    }

    @Test
    public void testLessOutput() throws IOException {
        List<Asset> results = pipe().from(fileSystem("src/test/resources/test.less", "glob:{**/,}*.{less}")).process(new LessProcessor()).as("less_test").to(new FileSystemDestination("target/dist")).run();

        for (Asset result : results) {
            System.out.println(result.getContent());
            System.out.println(result.getName());
            System.out.println(result.getCurrentPath().toString());
        }
    }

    @Test
    public void testLessAndJsReplaceOutput() throws IOException {
        List<Asset> results = pipe()
                .enrich(
                        new Pipeline().from(fileSystem("src/test/resources", "glob:{**/,}*.{js}"))
                                .process(new JavascriptProcessor())
                                .as("app_scripts")
                                .file("target/dist")
                )
                .enrich(
                        new Pipeline().from(fileSystem("src/test/resources", "glob:{**/,}*.{less}"))
                                .process(new LessProcessor())
                                .as("app_styles")
                                .file("target/dist")
                )
                .replace(
                        new Pipeline().from(fileSystem("src/test/resources", "glob:{**/,}*.{html}"))
                )
                .file("target/dist")
                .run();

        for (Asset result : results) {
            System.out.println(result.getContent());
            System.out.println(result.getName());
            System.out.println(result.getCurrentPath().toString());
        }
    }

    @Test
    public void testNewPiping() throws IOException {
        pipe().from(
                pipe().from(fileSystem("src/test/resources", "glob:{**/,}*.{js}"))
                        .process(new JavascriptProcessor())
                        .as("app_scripts")
                        .to(fileSystem("target/dist")),
                pipe().from(fileSystem("src/test/resources", "glob:{**/,}*.{less}"))
                        .process(new LessProcessor())
                        .as("app_styles")
                        .to(fileSystem("target/dist"))
        ).replace(
                pipe().from(fileSystem("src/test/resources", "glob:{**/,}*.{html}"))
        ).to(fileSystem("target/dist")).log().run();
    }

    @Test
    public void testTemplateCaching() throws IOException {
        pipe()
                .from(fileSystem("src/test/resources/js/templates", "glob:{**/,}*.{html}"))
                .process(templateCache("exampleApp", "templates.cache"))
                .log()
                .to(fileSystem("target/dist/templates"))
                .run();
    }

    @Test
    public void testSassOutput() throws IOException {
        List<Asset> results = pipe().from(singleFile("src/test/resources/test.scss")).process(new ScssProcessor()).as("sass_test").to(new FileSystemDestination("target/dist")).run();

        for (Asset result : results) {
            System.out.println(result.getContent());
            System.out.println(result.getName());
            System.out.println(result.getCurrentPath().toString());
        }
    }
}
