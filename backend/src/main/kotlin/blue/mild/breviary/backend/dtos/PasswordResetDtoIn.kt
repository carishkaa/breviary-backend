package blue.mild.breviary.backend.dtos

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * ResetPasswordDtoIn.
 *
 * @property token
 * @property newPassword
 */
data class PasswordResetDtoIn(
    @JsonProperty(required = true)
    val token: String,

    @JsonProperty(required = true)
    val newPassword: String
)
