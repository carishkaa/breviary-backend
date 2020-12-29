package blue.mild.breviary.backend.dtos

import blue.mild.breviary.backend.enums.InsulinType
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * InsulinPatientDtoIn.
 */
data class InsulinPatientDtoIn(
    @JsonProperty(required = true)
    val patient: PatientDtoIn,

    @JsonProperty(required = true)
    val targetGlycemia: Float,

    @JsonProperty(required = true)
    val tddi: Float,

    @JsonProperty(required = true)
    val insulinType: InsulinType
)
