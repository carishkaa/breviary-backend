package blue.mild.breviary.backend.dtos

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * HeparinPatientDtoOut.
 */
data class InsulinPatientDtoOut(
    @JsonProperty(required = true)
    val id: String,

    @JsonProperty(required = true)
    val patient: PatientDtoOut,

    @JsonProperty(required = true)
    val targetGlycemia: Int,

    @JsonProperty(required = true)
    val tddi: Int
)
