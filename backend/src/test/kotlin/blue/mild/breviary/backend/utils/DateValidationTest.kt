package blue.mild.breviary.backend.utils

import blue.mild.breviary.backend.errors.InvalidArgumentBreviaryException
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.test.assertFailsWith

class DateValidationTest {

    @Test
    fun `should validate that startTime must be smaller than endTime`() {
        val startTime = Instant.parse("2020-04-04T12:00:00Z")
        val endTime = Instant.parse("2020-04-04T13:00:00Z")

        validateStartTimeBeforeEndTime(startTime, endTime)

        assertFailsWith<InvalidArgumentBreviaryException> {
            validateStartTimeBeforeEndTime(startTime, startTime)
        }

        assertFailsWith<InvalidArgumentBreviaryException> {
            validateStartTimeBeforeEndTime(endTime, startTime)
        }
    }

    @Test
    fun `should validate that startTime date must be equal or smaller than validUntil date`() {
        val startTime = Instant.parse("2020-04-04T12:00:00Z")
        val validUntil1 = Instant.parse("2020-04-04T15:00:00Z")
        val validUntil2 = Instant.parse("2020-04-03T15:00:00Z")

        validateStartTimeNotAfterValidUntil(startTime, startTime)
        validateStartTimeNotAfterValidUntil(startTime, validUntil1)

        assertFailsWith<InvalidArgumentBreviaryException> {
            validateStartTimeBeforeEndTime(startTime, validUntil2)
        }
    }
}
