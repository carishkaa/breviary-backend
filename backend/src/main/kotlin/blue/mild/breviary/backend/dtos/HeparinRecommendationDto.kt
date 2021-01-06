package blue.mild.breviary.backend.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

/**
 * HeparinRecommendationDto.
 *
 * @param dosageContinuous amount of heparin solution to be applied continuously until the next check, in ml/h
 * @param dosageBolus amount of heparin solution to be applied immediately, in ml
 * @param nextRemainder time of next remainder
 * @param doctorWarning a warning that is displayed to the doctor if and unusual situation occurs
 */
data class HeparinRecommendationDto(
    @JsonProperty(required = true)
    val dosageContinuous: Float,

    @JsonProperty(required = true)
    val dosageBolus: Float,

    @JsonProperty(required = true)
    val nextRemainder: Instant,

    @JsonProperty(required = true)
    val doctorWarning: String
)
