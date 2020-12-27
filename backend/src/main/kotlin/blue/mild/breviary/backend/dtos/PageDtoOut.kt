package blue.mild.breviary.backend.dtos

import blue.mild.breviary.backend.enums.SortOrder
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * PageDtoOut.
 *
 * @param T
 * @property pagination
 * @property elements
 */
data class PageDtoOut<T>(
    @JsonProperty(required = true)
    val pagination: PaginationDtoOut,

    @JsonProperty(required = true)
    val elements: List<T>
)

/**
 * Gets empty page dto out.
 *
 * @param T
 * @param sortOrder
 * @param pageNumber
 * @param pageElements
 */
fun <T> getEmptyPageDtoOut(
    sortOrder: SortOrder,
    pageNumber: Int,
    pageElements: Int
) = PageDtoOut<T>(
    PaginationDtoOut(
        sortOrder = sortOrder,
        pageNumber = pageNumber,
        pageElements = pageElements,
        totalElements = 0,
        totalPages = 0
    ),
    elements = emptyList()
)
