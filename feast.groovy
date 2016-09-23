import io.diekema.dingo.feast.Asset

import static io.diekema.dingo.feast.DSL.fileSystem
import static io.diekema.dingo.feast.DSL.pipe;



def outputDir = "target/dist"
def inputDir = "src/test/resources"

def jsPipe = pipe().from(fileSystem(inputDir, "glob:{**/,}*.{js}")).flow("js.min").as("app_scripts").file(outputDir)
def lessPipe = pipe().from(fileSystem(inputDir, "glob:{**/,}*.{less}")).flow("less.compile").as("app_styles").file(outputDir)
def htmlPipe = pipe().from(fileSystem(inputDir, "glob:{**/,}*.{html}"));

def start = new Date()

List<Asset> results =
        pipe()
        .from(jsPipe, lessPipe)
        .replace(htmlPipe)
        .file(outputDir)
        .run()

for (Asset result in results) {
    print(result.getContent())
}

println "\nTotal Time:"
println new Date().getTime() - start.getTime() + "ms"