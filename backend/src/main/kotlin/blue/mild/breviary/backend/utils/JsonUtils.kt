package blue.mild.breviary.backend.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

/**
 * Standard [ObjectMapper] configured in a way the platform operates.
 */
fun jacksonMapper(): ObjectMapper = jacksonObjectMapper().apply {
    configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, false)
    registerModule(JavaTimeModule())
}

/**
 * Serializes given object to string.
 */
fun <T : Any> createJson(value: T): String = jacksonMapper().writeValueAsString(value)

/**
 * Serializes given object to byte array.
 */
fun <T : Any> createJsonBytes(value: T): ByteArray = jacksonMapper().writeValueAsBytes(value)

/**
 * Pretty print a json.
 */
fun prettyPrintJson(json: String): String = with(jacksonMapper()) {
    writerWithDefaultPrettyPrinter().writeValueAsString(readValue<Any>(json))
}
