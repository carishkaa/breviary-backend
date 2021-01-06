package blue.mild.breviary.backend.generators

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.support.ResourcePatternUtils
import org.springframework.web.context.WebApplicationContext
import pw.forst.tools.katlib.createPrettyJson
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

@SpringBootTest
class GenerateLocalizationTest(
    @Autowired private val resourceLoader: ResourceLoader,
    val context: WebApplicationContext
) {

    @Test
    fun generateSwagger() {
        val clientLocalizationFolder = "../client/localizations"
        Files.createDirectories(Paths.get(clientLocalizationFolder))
        ResourcePatternUtils
            .getResourcePatternResolver(resourceLoader)
            .getResources("file:src/main/resources/messages*.properties")
            .filter { it.isFile && it.filename != null }
            .map { it.file }
            .forEach { file ->
                val data = file
                    .readLines()
                    .associate {
                        val (key, value) = it
                            .split("=", limit = 2)
                            .map { e -> e.trim() }
                        key to value
                    }.toSortedMap()
                File("$clientLocalizationFolder/${file.name.replace("properties", "json")}")
                    .writeText(createPrettyJson(data))
            }
    }
}
