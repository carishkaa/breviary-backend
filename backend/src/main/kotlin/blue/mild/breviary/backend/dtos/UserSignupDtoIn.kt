package blue.mild.breviary.backend.dtos

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * UserSignupDtoIn.
 *
 */
data class UserSignupDtoIn(
    @JsonProperty(required = true)
    val email: String,

    @JsonProperty(required = true)
    val firstName: String,

    @JsonProperty(required = true)
    val lastName: String,

    @JsonProperty(required = true)
    val password: String
)
