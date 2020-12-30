/**
 * IgnoredFiles.
 */
@Suppress("SpreadOperator")
object IgnoredFiles {
    private val common: Array<String> = arrayOf(
        ""
    )

    val detekt: List<String> = listOf(
        *common
    )

    val jacoco: List<String> = listOf(
        *common
    )
}
