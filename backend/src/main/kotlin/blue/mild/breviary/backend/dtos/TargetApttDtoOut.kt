package blue.mild.breviary.backend.dtos

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * TargetApttDtoOut.
 */
data class TargetApttDtoOut(
    @JsonProperty(required = true)
    val low: Float,

    @JsonProperty(required = true)
    val high: Float
)
