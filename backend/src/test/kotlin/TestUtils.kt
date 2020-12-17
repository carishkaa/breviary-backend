import kotlin.math.abs
import kotlin.test.assertTrue

fun assertEquals(expected: Float, actual: Float, epsilon: Float, message: String? = null) {
    assertTrue(abs(actual - expected) < epsilon, message)
}
