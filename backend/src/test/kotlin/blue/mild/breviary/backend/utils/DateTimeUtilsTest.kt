package blue.mild.breviary.backend.utils

import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class DateTimeUtilsTest {

    @Test
    fun getContainedDaysTest() {
        val result = getContainedDays(
            LocalDate.of(2020, 1, 1),
            LocalDate.of(2020, 1, 20)
        )

        assertEquals(20, result.size)
        assertEquals(LocalDate.of(2020, 1, 1), result[0])
        assertEquals(LocalDate.of(2020, 1, 20), result[19])
    }
}
