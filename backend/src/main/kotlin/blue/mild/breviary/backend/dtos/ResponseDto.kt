package blue.mild.breviary.backend.dtos

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.Instant

/**
 * Error response.
 *
 * @property message
 */
@ApiModel(
    value = "ErrorResponseDto"
)
data class ResponseDto(
    /**
     * Message
     */
    @ApiModelProperty(value = "Message", required = true)
    val message: String,

    /**
     * Status code
     */
    @ApiModelProperty(value = "Status code", required = true)
    val statusCode: Int,

    /**
     * Timestamp
     */
    @ApiModelProperty(value = "Timestamp", required = true)
    val timestamp: String = Instant.now().toString(),

    /**
     * Payload
     */
    @ApiModelProperty(value = "Payload", required = true)
    val payload: PayloadDto = PayloadDto()
)
