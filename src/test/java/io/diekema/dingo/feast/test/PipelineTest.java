package io.diekema.dingo.feast.test;

import io.diekema.dingo.feast.Asset;
import io.diekema.dingo.feast.Pipeline;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by rdiekema on 8/23/16.
 */
public class PipelineTest {

    @Test
    public void testNoOp() throws IOException {
        Pipeline pipeline = new Pipeline();

        pipeline.from("src/test/resources", "glob:{**/,}*.{scss,sass,js,css}")
                .pipe("text.concat")
                .to("target/out");

        List<Asset> results = pipeline.run();

        for (Asset result : results) {
            System.out.println(result.getContent());
            System.out.println(result.getName());
            System.out.println(result.getCurrentPath().toString());
        }
    }

    @Test
    public void testClosureCompiler() throws IOException {

        Pipeline pipeline = new Pipeline();
        pipeline
                .from("src/test/resources", "glob:{**/,}*.{js}")
                .pipe("js.min")
                .to("target/out");


        List<Asset> results = pipeline.run();

        for (Asset result : results) {
            System.out.println(result.getContent());
            System.out.println(result.getName());
            System.out.println(result.getCurrentPath().toString());
        }
    }

    @Test
    public void testJsMinifyRename() throws IOException {
        Pipeline pipeline = new Pipeline();
        pipeline
                .from("src/test/resources", "glob:{**/,}*.{js}")
                .pipe("js.min")
                .as("app.min.js")
                .to("target/out");


        List<Asset> results = pipeline.run();

        for (Asset result : results) {
            System.out.println(result.getContent());
            System.out.println(result.getName());
            System.out.println(result.getCurrentPath().toString());
        }
    }

    @Test
    public void testHtmlOutput() throws IOException {
        Pipeline pipeline = new Pipeline();

        pipeline.from("src/test/resources", "glob:{**/,}*.{html}")
                .to("target/out");

        List<Asset> results = pipeline.run();

        for (Asset result : results) {
            System.out.println(result.getContent());
            System.out.println(result.getName());
            System.out.println(result.getCurrentPath().toString());
        }
    }

    @Test
    public void testHtmlReferenceReplace() throws IOException {
        Pipeline pipeline = new Pipeline();
        pipeline
                .from("src/test/resources", "glob:{**/,}*.{js}")
                .pipe("js.min")
                .as("app.min.js")
                .to("target/out")
                .replace(new Pipeline().from("src/test/resources", "glob:{**/,}*.{html}"))
                .to("target/out");


        List<Asset> results = pipeline.run();

        for (Asset result : results) {
            System.out.println(result.getContent());
            System.out.println(result.getName());
            System.out.println(result.getCurrentPath().toString());
        }
    }

    @Test
    public void testLessOutput() throws IOException {
        Pipeline pipeline = new Pipeline();
        pipeline.from("src/test/resources", "glob:{**/,}*.{less}").pipe("less.compile").as("app.css").to("target/out");

        List<Asset> results = pipeline.run();

        for (Asset result : results) {
            System.out.println(result.getContent());
            System.out.println(result.getName());
            System.out.println(result.getCurrentPath().toString());
        }
    }

    @Test
    public void testLessAndJsReplaceOutput() throws IOException {
        Pipeline pipeline = new Pipeline();
        pipeline
                .enrich(new Pipeline().from("src/test/resources", "glob:{**/,}*.{js}")
                        .pipe("js.min")
                        .as("app_scripts")
                        .to("target/out"))
                .enrich(new Pipeline().from("src/test/resources", "glob:{**/,}*.{less}")
                        .pipe("less.compile")
                        .as("app_styles")
                        .to("target/out"))
                .replace(new Pipeline().from("src/test/resources", "glob:{**/,}*.{html}"))
                .to("target/out");


        List<Asset> results = pipeline.run();

        for (Asset result : results) {
            System.out.println(result.getContent());
            System.out.println(result.getName());
            System.out.println(result.getCurrentPath().toString());
        }
    }
}
