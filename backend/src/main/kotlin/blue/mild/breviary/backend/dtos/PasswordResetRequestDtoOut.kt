package blue.mild.breviary.backend.dtos

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * PasswordResetRequestDtoOut.
 *
 * @property message
 */
data class PasswordResetRequestDtoOut(
    @JsonProperty(required = true)
    val message: String
)
