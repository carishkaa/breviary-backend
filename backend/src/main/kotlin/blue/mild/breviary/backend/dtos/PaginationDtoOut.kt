package blue.mild.breviary.backend.dtos

import blue.mild.breviary.backend.enums.SortOrder
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * PaginationDtoOut.
 *
 * @property sortOrder
 * @property pageNumber
 * @property pageElements
 * @property totalElements
 * @property totalPages
 */
data class PaginationDtoOut(
    @JsonProperty(required = true)
    val sortOrder: SortOrder,

    @JsonProperty(required = true)
    val pageNumber: Int,

    @JsonProperty(required = true)
    val pageElements: Int,

    @JsonProperty(required = true)
    val totalElements: Long,

    @JsonProperty(required = true)
    val totalPages: Int
)
