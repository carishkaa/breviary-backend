package blue.mild.breviary.backend.utils

import blue.mild.breviary.backend.Project
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class StringUtilsTest {

    @Test
    fun `should test if string is null empty`() {
        assertTrue(isNullOrEmpty(null))
        assertTrue(isNullOrEmpty(""))
        assertFalse(isNullOrEmpty("something"))
    }

    @Test
    fun `should test if string is not null or empty`() {
        assertFalse(isNotNullOrEmpty(null))
        assertFalse(isNotNullOrEmpty(""))
        assertTrue(isNotNullOrEmpty("something"))
    }

    @Test
    fun `should test valid email address`() {
        assertTrue("testee@example.org".isEmail())
        assertTrue("testee+me1@example.org".isEmail())
        assertTrue("testee.me1@example.org".isEmail())
        assertTrue("testee@example.co.uk".isEmail())

        assertFalse("@example.org".isEmail())
        assertFalse("example.org".isEmail())
        assertFalse("example@org".isEmail())
        assertFalse("example".isEmail())
        assertFalse("".isEmail())
    }

    @Test
    fun `simple test randomLettersOnlyString`() {
        val length = 10
        val generated = randomLettersOnlyString(length)
        assertEquals(length, generated.length)
    }

    @Test
    fun `should transform string to byte buffer and back`() {
        val data = "Hi all!"
        val result = data.toEncodedByteBuffer().toDecodedString()
        assertEquals(data, result)
    }

    @Test
    fun `should transform byte array to byte buffer and back`() {
        val data = "Hi all!"
        val array = data.toByteArray(Project.STRING_CHARSET)
        val result = array.toEncodedByteBuffer().toDecodedString()
        assertEquals(data, result)
    }

    @Test
    fun `should transform string to byte array and back`() {
        val data = "Hi all!"
        val result = data.toByteArray().toDecodedString()
        assertEquals(data, result)
    }

    @Test
    fun `should generate random string`() {
        val str1 = generateRandomString()
        val str2 = generateRandomString()
        assertEquals(16, str1.length)
        assertEquals(16, str2.length)
        assertNotEquals(str1, str2)
    }
}
