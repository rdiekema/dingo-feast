import io.diekema.dingo.feast.Asset

import static io.diekema.dingo.feast.DSL.fileSystem
import static io.diekema.dingo.feast.DSL.pipe;

List<Asset> results =
        pipe()
                .enrich(pipe().from(fileSystem("src/test/resources", "glob:{**/,}*.{js}"))
                .flow("js.min")
                .as("app_scripts")
                .file("target/dist"))

                .enrich(pipe().from(fileSystem("src/test/resources", "glob:{**/,}*.{less}"))
                .flow("less.compile")
                .as("app_styles")
                .file("target/dist"))

                .replace(pipe().from(fileSystem("src/test/resources", "glob:{**/,}*.{html}")))
                .file("target/dist")
                .run()

for (Asset result in results) {
    print(result.getContent())
}
