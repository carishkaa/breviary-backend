package blue.mild.breviary.backend.dtos

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * PasswordChangeDtoIn.
 */
data class PasswordChangeDtoIn(
    @JsonProperty(required = true)
    val currentPassword: String,

    @JsonProperty(required = true)
    val newPassword: String,

    @JsonProperty(required = true)
    val newPasswordAgain: String
)
