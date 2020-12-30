package blue.mild.breviary.backend.services

import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

/**
 *  Implementation of [TimeProvider] providing access to [Instant.now] method.
 */
@Service
class InstantTimeProvider : TimeProvider<Instant> {

    companion object {
        val instance = InstantTimeProvider()
    }

    override fun now(): Instant = OffsetDateTime.now(ZoneOffset.UTC).toInstant()
}

/**
 * Convert to local datetime in UTC.
 *
 */
fun Instant.toLocalDateTimeInUTC(): LocalDateTime = LocalDateTime.ofInstant(this, ZoneOffset.UTC)

/**
 * Convert to local datetime in target timezone.
 *
 */
fun Instant.toLocalDateTimeInTimeZone(zoneId: String): LocalDateTime = LocalDateTime.ofInstant(this, ZoneId.of(zoneId))

/**
 * Convert to local date in UTC.
 *
 */
fun Instant.toLocalDateInUTC(): LocalDate = this.toLocalDateTimeInUTC().toLocalDate()

/**
 * Convert to local date in target timezone.
 *
 */
fun Instant.toLocalDateInTimeZone(zoneId: String): LocalDate = this.toLocalDateTimeInTimeZone(zoneId).toLocalDate()

/**
 * Convert to Instant with UTC zone offset.
 *
 * @return
 */
fun LocalDateTime.toInstantInUTC(): Instant = this.toInstant(ZoneOffset.UTC)

/**
 * Convert to Instant with custom timezone offset.
 *
 * @return
 */
fun LocalDateTime.toInstantFromTimeZoneTimeZone(zoneId: String): Instant = this.atZone(ZoneId.of(zoneId)).toInstant()

/**
 * Convert to Instant at start of the day with UTC zone offset.
 *
 * @return
 */
fun LocalDate.toInstantInUTC(): Instant = this.atStartOfDay().toInstantInUTC()

/**
 * Gets start of day.
 *
 * @return
 */
fun Instant.getStartOfDay(): Instant = this.truncatedTo(ChronoUnit.DAYS)
