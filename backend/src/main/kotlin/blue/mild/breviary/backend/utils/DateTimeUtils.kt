package blue.mild.breviary.backend.utils

import java.time.LocalDate

/**
 * Returns a set of local dates between start and end inclusive.
 *
 * @return
 */
fun getContainedDays(start: LocalDate, end: LocalDate): List<LocalDate> {
    return generateSequence(start) { d ->
        d.plusDays(1).takeIf { it <= end }
    }.toList()
}
