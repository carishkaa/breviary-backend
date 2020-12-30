package blue.mild.breviary.backend.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

/**
 * HeparinRecommendationDtoOut.
 */
data class HeparinRecommendationDtoOut(
    @JsonProperty(required = true)
    val actualHeparinContinuousDosage: Float,

    @JsonProperty(required = true)
    val previousHeparinContinuousDosage: Float?,

    @JsonProperty(required = true)
    val actualHeparinBolusDosage: Float,

    @JsonProperty(required = true)
    val previousHeparinBolusDosage: Float?,

    @JsonProperty(required = true)
    val nextRemainder: Instant,

    @JsonProperty(required = true)
    val doctorWarning: String
)
