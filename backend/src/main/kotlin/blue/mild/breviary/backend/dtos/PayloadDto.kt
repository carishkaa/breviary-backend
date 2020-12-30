package blue.mild.breviary.backend.dtos

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * Error response payload.
 *
 * @property formFieldErrors
 */
@ApiModel(
    value = "PayloadDto"
)
data class PayloadDto(
    /**
     * Form errors. Key is the form field name, value is anonymized error message shown to the user.
     */
    @ApiModelProperty(value = "Form field errors", required = false)
    val formFieldErrors: HashMap<String, String>? = null
)
