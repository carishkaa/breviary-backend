package blue.mild.breviary.backend.dtos

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * InsulinPatientWithDataDtoOut.
 */
data class InsulinPatientWithDataDtoOut(
    @JsonProperty(required = true)
    val insulinPatient: InsulinPatientDtoOut
)
