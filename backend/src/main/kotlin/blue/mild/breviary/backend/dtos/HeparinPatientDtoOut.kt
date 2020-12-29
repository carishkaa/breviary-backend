package blue.mild.breviary.backend.dtos

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * HeparinPatientDtoOut.
 */
data class HeparinPatientDtoOut(
    @JsonProperty(required = true)
    val id: String,

    @JsonProperty(required = true)
    val patient: PatientDtoOut,

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
