package blue.mild.breviary.backend.generators

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup
import org.springframework.web.context.WebApplicationContext
import java.io.File

@SpringBootTest
class GenerateSwaggerTest(val context: WebApplicationContext) {

    @Value(value = "\${springfox.documentation.swagger.v3.path}")
    val docApi: String? = null

    @Test
    fun generateSwagger() {
        val mockMvc = webAppContextSetup(context).build()
        mockMvc.perform(MockMvcRequestBuilders.get(docApi!!).accept(MediaType.APPLICATION_JSON))
            .andDo { result ->
                val swaggerContent = formatJson(result.response.contentAsString)
                File("../swagger.json").writeText(swaggerContent)
            }
    }

    private fun formatJson(input: String): String {
        val mapper = ObjectMapper()
        val json = mapper.readValue(input, Any::class.java)
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json)
    }
}
