package blue.mild.breviary.backend.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

/**
 * InsulinRecommendationDto.
 */
data class InsulinRecommendationDto(
    @JsonProperty(required = true)
    val dosageHeparinContinuous: Float,

    @JsonProperty(required = true)
    val dosageHeparinBolus: Float,

    @JsonProperty(required = true)
    val nextRemainder: Instant,

    @JsonProperty(required = true)
    val doctorWarning: String
)
