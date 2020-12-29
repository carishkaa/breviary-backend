package blue.mild.breviary.backend.utils

import blue.mild.breviary.backend.Project
import java.nio.ByteBuffer
import java.util.regex.Pattern.compile
import kotlin.random.Random

/**
 * Check if string is valid email address.
 *
 * @return
 */
fun String.isEmail(): Boolean = emailRegex.matcher(this).matches()

// Taken from Android/Kotlin lib
private val emailRegex = compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)

/**
 * List of possible characters for [randomLettersOnlyString].
 */
val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

/**
 * Generates random string with given length. If no [random] is passed, default instance of [Random] is used.
 */
fun randomLettersOnlyString(len: Int, random: Random? = null): String =
    (1..len).map { (random ?: Random).nextInt(0, charPool.size) }.joinToString("") { charPool[it].toString() }

/**
 * Convert list of strings to string like this "['one','two','three']".
 *
 * @param versions list of strings to print
 * @return String representation of List
 */
fun listToString(versions: List<String>): String = versions.joinToString(prefix = "[", postfix = "]") { "\'$it\'" }

/**
 * Converts [ByteBuffer] to decoded [String].
 */
fun ByteBuffer.toDecodedString(): String = Project.STRING_CHARSET.decode(this).toString()

/**
 * Converts [String] to decoded [ByteBuffer].
 *
 */
fun String.toEncodedByteBuffer(): ByteBuffer = ByteBuffer.wrap(this.toByteArray(Project.STRING_CHARSET))

/**
 * Converts [ByteArray] to decoded [String].
 *
 */
fun ByteArray.toDecodedString(): String = this.toString(Project.STRING_CHARSET)

/**
 * Converts [ByteArray] to decoded [ByteBuffer].
 *
 */
fun ByteArray.toEncodedByteBuffer(): ByteBuffer = ByteBuffer.wrap(this)

/**
 * Generates random string.
 *
 * @return
 */
fun generateRandomString(len: Int = 16): String = (1..len)
    .map { Random.nextInt(0, charPool.size) }
    .map(charPool::get)
    .joinToString("")
