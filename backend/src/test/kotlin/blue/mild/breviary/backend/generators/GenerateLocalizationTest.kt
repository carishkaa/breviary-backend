package blue.mild.breviary.backend.generators

import blue.mild.breviary.backend.utils.createJson
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.support.ResourcePatternUtils
import org.springframework.web.context.WebApplicationContext
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

@SpringBootTest
class GenerateLocalizationTest(
    @Autowired private val resourceLoader: ResourceLoader,
    val context: WebApplicationContext
) {

    @Suppress("NestedBlockDepth")
    @Test
    fun generateSwagger() {
        val clientLocalizationFolder = "../client/localizations"
        Files.createDirectories(Paths.get(clientLocalizationFolder))
        val resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
            .getResources("file:src/main/resources/messages*.properties")
        resources
            .filter { it.isFile && it.filename != null }
            .forEach {
                run {
                    val data = HashMap<String, String>()
                    it.file.readLines().forEach { line ->
                        run {
                            val keyValue = line.split("=", limit = 2).map { e -> e.trim() }
                            data[keyValue[0]] = keyValue[1]
                        }
                    }
                    val jsonData = formatJson(createJson(data.toSortedMap()))
                    File("$clientLocalizationFolder/${it.filename!!.replace("properties", "json")}").writeText(
                        jsonData
                    )
                }
            }
    }

    private fun formatJson(input: String): String {
        val mapper = ObjectMapper()
        val json = mapper.readValue(input, Any::class.java)
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json)
    }
}
