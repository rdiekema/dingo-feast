import io.diekema.dingo.feast.*

Pipeline pipeline = new Pipeline()
pipeline.from("src/test/resources", "glob:{**/,}*.{js}")
        .pipe("js.min")
        .as("app.min.js")
        .to("target/out")
        .replace(new Pipeline().from("src/test/resources", "glob:{**/,}*.{html}"), "script.app")
        .to("./target/out");


List<Asset> results = pipeline.run();

for(Asset result in results){
     print(result.getContent())
}
