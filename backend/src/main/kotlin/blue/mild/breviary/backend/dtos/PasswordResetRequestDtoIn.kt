package blue.mild.breviary.backend.dtos

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * PasswordResetRequestDtoIn.
 *
 * @property username
 */
data class PasswordResetRequestDtoIn(
    @JsonProperty(required = true)
    val username: String
)
