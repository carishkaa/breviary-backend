package blue.mild.breviary.backend.enums

import org.springframework.data.domain.Sort

/**
 * SortOrder enum.
 *
 */
enum class SortOrder {
    ASC,
    DESC
}

const val DEFAULT_SORT_ORDER: String = "DESC"
const val DEFAULT_PAGE_NUMBER: Int = 0
const val DEFAULT_PAGE_ELEMENTS: Int = 100
const val DEFAULT_MAX_PAGE_ELEMENTS: Int = Int.MAX_VALUE
val DEFAULT_SORT_DIRECTION = Sort.Direction.ASC

/**
 * Function to convert custom sort order to Spring JPA enum.
 *
 * @return
 */
fun SortOrder.toSpring(): Sort.Direction = if (this == SortOrder.ASC) Sort.Direction.ASC else Sort.Direction.DESC
