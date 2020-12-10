package blue.mild.breviary.backend.utils

import blue.mild.breviary.backend.PropertiesNames
import blue.mild.breviary.backend.dtos.PayloadDto
import blue.mild.breviary.backend.errors.InvalidArgumentBreviaryException
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * StartTime < endTime validation.
 *
 * @param startTime
 * @param endTime
 */
@Throws(InvalidArgumentBreviaryException::class)
fun validateStartTimeBeforeEndTime(startTime: Instant, endTime: Instant) {
    if (startTime >= endTime) {
        throw InvalidArgumentBreviaryException(
            "'$startTime' must be smaller than '$endTime.",
            payload = PayloadDto(
                hashMapOf(
                    PropertiesNames.START_TIME to "'$startTime' must be smaller than '$endTime.",
                    PropertiesNames.END_TIME to "'$endTime' must be larger than '$startTime."
                )
            )
        )
    }
}

/**
 * StartTime date <= validUntil date validation.
 *
 * @param startTime
 * @param validUntil
 */
@Throws(InvalidArgumentBreviaryException::class)
fun validateStartTimeNotAfterValidUntil(startTime: Instant, validUntil: Instant) {
    if (startTime.truncatedTo(ChronoUnit.DAYS) > validUntil.truncatedTo(ChronoUnit.DAYS)) {
        throw InvalidArgumentBreviaryException(
            "'$startTime' date must be smaller or equal than '$validUntil date.",
            payload = PayloadDto(
                hashMapOf(
                    PropertiesNames.START_TIME to "'$startTime' date must be smaller or equal than '$validUntil date.",
                    PropertiesNames.VALID_UNTIL to "'$validUntil' date must be larger or equal than '$startTime date."
                )
            )
        )
    }
}
