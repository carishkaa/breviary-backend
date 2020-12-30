package blue.mild.breviary.backend.domain

import kotlin.math.ceil

/**
 * Class representing the custom pagination.
 */
class CustomPagination<T>(
    private val records: List<T>,
    val pageNumber: Int,
    val pageElements: Int
) {
    /**
     * Returns one page.
     *
     * @return
     */
    fun getOnePage(): PageInfo<T> {
        val indexFrom = Integer.min((pageNumber - 1) * pageElements + pageElements, records.size)
        val indexTo = Integer.min(indexFrom + pageElements, records.size)
        val recordsForOnePage = records.subList(indexFrom, indexTo)
        val totalElements = records.size.toLong()
        val totalPages = if (pageElements == 0) 1 else {
            ceil(totalElements.toDouble() / pageElements.toDouble()).toInt()
        }

        return PageInfo(recordsForOnePage, totalElements, totalPages)
    }
}

/**
 * Class representing a page of values.
 */
data class PageInfo<T>(
    val recordsForOnePage: List<T>,
    val totalElements: Long,
    val totalPages: Int
)
