package blue.mild.breviary.backend.services

import blue.mild.breviary.backend.TimeZones
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.Instant
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InstantTimeProviderTest(@Autowired private val instantTimeProvider: InstantTimeProvider) : ServiceTest() {

    @Test
    fun `should return now instance is in correct interval`() {
        val testStart = Instant.now()
        val providedNow = instantTimeProvider.now()
        val testEnd = Instant.now()
        assertTrue { testStart <= providedNow }
        assertTrue { providedNow <= testEnd }
    }

    @Test
    fun `should convert Instant to LocalDateTime`() {
        val result1 = Instant.parse("2020-04-03T12:00:00Z").toLocalDateTimeInUTC()
        assertEquals(2020, result1.year)
        assertEquals(4, result1.monthValue)
        assertEquals(3, result1.dayOfMonth)
        assertEquals(12, result1.hour)
        assertEquals(0, result1.minute)

        val result2 = Instant.parse("2020-04-03T23:00:00Z").toLocalDateTimeInTimeZone(TimeZones.PRAGUE)
        assertEquals(2020, result2.year)
        assertEquals(4, result2.monthValue)
        assertEquals(4, result2.dayOfMonth)
        assertEquals(1, result2.hour)
        assertEquals(0, result2.minute)
    }

    @Test
    fun `should convert Instant to LocalDate`() {
        val result1 = Instant.parse("2020-04-03T12:00:00Z").toLocalDateInUTC()
        assertEquals(2020, result1.year)
        assertEquals(4, result1.monthValue)
        assertEquals(3, result1.dayOfMonth)

        val result2 = Instant.parse("2020-04-03T23:00:00Z").toLocalDateInTimeZone(TimeZones.PRAGUE)
        assertEquals(2020, result2.year)
        assertEquals(4, result2.monthValue)
        assertEquals(4, result2.dayOfMonth)
    }

    @Test
    fun `should convert LocalDateTime to Instant`() {
        val result1 = LocalDateTime.of(2020, 3, 1, 0, 0, 0).toInstantInUTC()
        assertEquals(1_583_020_800, result1.epochSecond)

        val result2 = LocalDateTime.of(2020, 3, 1, 0, 0, 0).toInstantFromTimeZoneTimeZone(TimeZones.PRAGUE)
        assertEquals(1_583_017_200, result2.epochSecond)
    }

    @Test
    fun `should get start of day`() {
        val date = Instant.parse("2020-04-03T12:00:00Z")
        val result = date.getStartOfDay()
        assertEquals(Instant.parse("2020-04-03T00:00:00Z"), result)
    }
}
