package blue.mild.breviary.backend.dtos

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * InsulinRecommendationDtoIn.
 */
data class InsulinRecommendationDtoIn(
    @JsonProperty(required = true)
    val insulinPatientId: String,

    @JsonProperty(required = true)
    val currentGlycemia: Float,

    @JsonProperty(required = true)
    val expectedCarbohydrateIntake: Float
)
