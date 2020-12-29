package blue.mild.breviary.backend.dtos

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * LoginDtoIn.
 */
data class LoginDtoIn(
    @JsonProperty(required = true)
    val email: String,

    @JsonProperty(required = true)
    val password: String
)
