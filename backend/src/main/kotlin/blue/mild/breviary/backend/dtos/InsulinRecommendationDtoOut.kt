package blue.mild.breviary.backend.dtos

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * InsulinRecommendationDtoOut.
 */
data class InsulinRecommendationDtoOut(
    @JsonProperty(required = true)
    val dosage: Float
)
