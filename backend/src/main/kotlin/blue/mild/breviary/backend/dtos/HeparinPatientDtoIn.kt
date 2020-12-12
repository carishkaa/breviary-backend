package blue.mild.breviary.backend.dtos

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * HeparinPatientDtoIn.
 */
data class HeparinPatientDtoIn(
    @JsonProperty(required = true)
    val patient: PatientDtoIn,

    @JsonProperty(required = true)
    val targetApptLow: Int,

    @JsonProperty(required = true)
    val targetApptHigh: Int,

    @JsonProperty(required = true)
    val solutionHeparinIu: Int,

    @JsonProperty(required = true)
    val solutionMl: Int,

    @JsonProperty(required = true)
    val weight: Int
)
