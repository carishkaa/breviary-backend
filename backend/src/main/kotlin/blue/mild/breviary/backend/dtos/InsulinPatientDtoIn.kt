package blue.mild.breviary.backend.dtos

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * InsulinPatientDtoIn.
 */
data class InsulinPatientDtoIn(
    @JsonProperty(required = true)
    val patient: PatientDtoIn,

    @JsonProperty(required = true)
    val targetGlycemia: Int,

    @JsonProperty(required = true)
    val tddi: Int
)
