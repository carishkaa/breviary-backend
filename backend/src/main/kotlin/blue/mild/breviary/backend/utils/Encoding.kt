package blue.mild.breviary.backend.utils

import blue.mild.breviary.backend.Project
import java.util.Base64

const val SPLIT_DELIMITER = "|"

/**
 * Encodes long to Base64 key.
 *
 * @param suffix
 * @return
 */
fun Long.encodeID(suffix: String): String {
    val encoded = Base64.getEncoder()
        .encodeToString(
            "$this$SPLIT_DELIMITER$suffix"
                .toByteArray(Project.STRING_CHARSET)
        )
    var result = encoded
    base64Replacements.forEach {
        result = result.replace(it.key, it.value)
    }
    return result
}

/**
 * Decodes key to long.
 *
 * @return
 */
fun String.decodeID(): Long {
    var decoded = this
    base64Replacements.forEach {
        decoded = decoded.replace(it.value, it.key)
    }

    return Base64.getDecoder()
        .decode(decoded)
        .toString(Project.STRING_CHARSET)
        .split(SPLIT_DELIMITER).first().toLong()
}

private val base64Replacements = mapOf(
    "+" to ".",
    "/" to "_",
    "=" to "-"
)
