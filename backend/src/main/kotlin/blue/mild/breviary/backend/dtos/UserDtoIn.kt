package blue.mild.breviary.backend.dtos

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * UserDtoIn.
 */
data class UserDtoIn(
    @JsonProperty(required = true)
    val email: String,

    @JsonProperty(required = true)
    val password: String
)
