package blue.mild.breviary.backend.utils

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.test.assertEquals

data class A(val int: Int, val string: String, val double: Double)

data class B(val stringList: List<String>, val intSet: Set<Int>, val map: Map<Int, String>, val a: A)
data class Generic<T>(val t: T, val ts: List<T>)

class JsonFunctionsTest {

    @Test
    fun `should serialize and deserialize json`() {
        val input = Instant.now()
        val serialized = jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .writeValueAsString(input)
        val deserialized = jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .readValue(serialized, Instant::class.java)

        assertEquals(input, deserialized)
    }


    @Test
    fun `should pretty print json`() {
        val lineSeparator = System.lineSeparator()
        val json =
            "{\"int\": 123, \"string\": \"my home\", \"double\": 1.0, \"object\": {\"property\": \"value\", \"second property\": \"value\"}}"

        val expected = "{$lineSeparator" +
                "  \"int\" : 123,$lineSeparator" +
                "  \"string\" : \"my home\",$lineSeparator" +
                "  \"double\" : 1.0,$lineSeparator" +
                "  \"object\" : {$lineSeparator" +
                "    \"property\" : \"value\",$lineSeparator" +
                "    \"second property\" : \"value\"$lineSeparator" +
                "  }$lineSeparator" +
                "}"
        val actual = prettyPrintJson(json)
        assertEquals(expected, actual)
    }

    @Test
    fun `should create minimal json bytes`() {
        val result = createJsonBytes(mapOf("a" to "b", "c" to "d", "e" to mapOf("f" to "g"))).toString()
        assert(!result.contains(Regex("\\s"))) {
            "Should not contain whitespaces"
        }
    }
}
