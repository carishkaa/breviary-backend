package blue.mild.breviary.backend.services

import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import java.time.ZoneOffset
import java.util.TimeZone
import javax.annotation.PostConstruct

/**
 * This sets the same time zone - UTC to all of the microservices.
 */
@Component
@Lazy(false)
class SameTimeZoneSetter {

    /**
     * Set the UTC time zone after start.
     */
    @PostConstruct
    fun setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC))
    }
}
