package blue.mild.breviary.backend.dtos

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * HeparinRecommendationDtoIn.
 */
data class HeparinRecommendationDtoIn(
    @JsonProperty(required = true)
    val heparinPatientId: String,

    @JsonProperty(required = true)
    val currentAptt: Float
)
