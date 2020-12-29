package blue.mild.breviary.backend.dtos

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * HeparinPatientDtoIn.
 */
data class HeparinPatientDtoIn(
    @JsonProperty(required = true)
    val patient: PatientDtoIn,

    @JsonProperty(required = true)
    val targetApttLow: Float,

    @JsonProperty(required = true)
    val targetApttHigh: Float,

    @JsonProperty(required = true)
    val solutionHeparinUnits: Float,

    @JsonProperty(required = true)
    val solutionMilliliters: Float,

    @JsonProperty(required = true)
    val weight: Float
)
