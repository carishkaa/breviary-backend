package blue.mild.breviary.backend.dtos

import blue.mild.breviary.backend.enums.UserRole
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * UserDtoOut.
 */
data class UserDtoOut(
    @JsonIgnore
    @JsonProperty(required = false)
    val id: Long = 0,

    @JsonProperty(required = true)
    val email: String,

    @JsonIgnore
    @JsonProperty(required = false)
    val password: String = "",

    @JsonProperty(required = true)
    val roles: Set<UserRole>,

    @JsonIgnore
    @JsonProperty(required = false)
    val active: Boolean = true
)
