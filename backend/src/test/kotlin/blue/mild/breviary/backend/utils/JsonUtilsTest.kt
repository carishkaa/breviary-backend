package blue.mild.breviary.backend.utils

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.fail

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
    fun `should parse generic class`() {
        val genericInstance = Generic("hello", listOf("hello1", "hello2"))
        val json = "{\n" +
                "\t\"t\": \"hello\",\n" +
                "\t\"ts\": [\"hello1\", \"hello2\"]\n" +
                "}"
        val actual = parseJson<Generic<String>>(json)
        assertEquals(genericInstance, actual)
    }

    @Test
    fun `should parse int map`() {
        val mapper = jacksonMapper()

        val expected: Map<Int, String> = mapOf(1 to "hello", 2 to "world")

        val jsonified = mapper.writeValueAsString(expected)

        val actual: Map<Int, String> = parseJson(jsonified) ?: fail("json not parsed")

        assertEquals(expected, actual)
    }

    @Test
    fun `should parse A`() {
        val json = "{\n" +
                "\t\"int\":1,\n" +
                "\t\"string\": \"my home\",\n" +
                "\t\"double\":1.2\n" +
                "}"

        val expected = A(1, "my home", 1.2)
        val actual = parseJson<A>(json)
        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun `should not parse A`() {
        val json = "{\n" +
                "\t\"int\":1.125,\n" +
                "\t\"string\": \"my home\",\n" +
                "\t\"double\":1.2\n" +
                "}"

        val actual = parseJson<A>(json)
        assertNull(actual)
    }

    @Test
    fun `should parse B`() {
        val json = "{\n" +
                "\t\"stringList\":[\"one\", \"two\", \"three\"],\n" +
                "\t\"intSet\": [1,2,3],\n" +
                "\t\"map\": {\n" +
                "\t\t\"1\": \"hello\",\n" +
                "\t\t\"2\": \"hello world\"\n" +
                "\t},\n" +
                "\t\"a\": {\n" +
                "\t\t\"int\":1,\n" +
                "\t\t\"string\": \"my home\",\n" +
                "\t\t\"double\":1.0\n" +
                "\t}\n" +
                "}"

        val expected = B(
            listOf("one", "two", "three"),
            setOf(1, 2, 3),
            mapOf(1 to "hello", 2 to "hello world"),
            A(1, "my home", 1.0)
        )
        val actual = parseJson<B>(json)
        assertNotNull(actual)
        assertEquals(expected, actual)
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
