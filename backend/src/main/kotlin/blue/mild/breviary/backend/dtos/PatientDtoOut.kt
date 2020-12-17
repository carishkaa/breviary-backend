package blue.mild.breviary.backend.dtos

import blue.mild.breviary.backend.enums.Sex
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
import java.util.HashMap

@Suppress("LongParameterList")
/**
 * PatientDtoOut.
 */
open class PatientDtoOut(
    @JsonProperty(required = false)
    @JsonIgnore
    val id: String,

    @JsonProperty(required = true)
    val firstName: String,

    @JsonProperty(required = true)
    val lastName: String,

    @JsonProperty(required = true)
    val dateOfBirth: Instant,

    @JsonProperty(required = true)
    val height: Int,

    @JsonProperty(required = true)
    val sex: Sex,

    @JsonProperty(required = true)
    val otherParams: HashMap<String, String>
)
