package blue.mild.breviary.backend.extensions

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ListExtensionsTest {

    @Test
    fun testCartesianProduct() {
        val input = listOf(
            listOf(1, 3, 5),
            listOf(2),
            listOf(4, 6)
        )
        val expected = listOf(
            listOf(1, 2, 4),
            listOf(1, 2, 6),
            listOf(3, 2, 4),
            listOf(3, 2, 6),
            listOf(5, 2, 4),
            listOf(5, 2, 6)
        )
        val actual = input.cartesianProduct()
        assertEquals(expected, actual)

        val actualLazy = input.lazyCartesianProduct()
        assertEquals(expected, actualLazy.toList())
    }

    @Test
    fun testCartesianProductWithEmptyList() {
        val input = listOf(
            listOf(1, 3, 5),
            listOf(),
            listOf(4, 6)
        )
        val expected = listOf<List<Int>>()
        val actual = input.cartesianProduct()
        assertEquals(expected, actual)

        val actualLazy = input.lazyCartesianProduct()
        assertEquals(expected, actualLazy.toList())
    }
}
