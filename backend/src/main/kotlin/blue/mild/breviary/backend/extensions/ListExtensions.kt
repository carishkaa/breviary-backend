package blue.mild.breviary.backend.extensions

/**
 * List cartesian production.
 *
 * @see: https://stackoverflow.com/a/53763936/41071
 * @param T
 * @return
 */
fun <T> List<List<T>>.cartesianProduct(): List<List<T>> =
    if (this.isEmpty()) emptyList()
    else this.fold(listOf(listOf())) { acc, set ->
        acc.flatMap { list -> set.map { element -> list + element } }
    }

private fun <T> lazyCartesianProductAcc(l: List<List<T>>, acc: List<T>): Sequence<List<T>> = sequence {
    if (l.isEmpty()) {
        yield(acc)
    } else {
        val rest = l.drop(1)
        val variants = l.first().asSequence().flatMap { lazyCartesianProductAcc(rest, acc + it) }
        yieldAll(variants)
    }
}

/**
 * Lazy cartesian production.
 *
 * @param T
 * @return
 */
fun <T> List<List<T>>.lazyCartesianProduct(): Sequence<List<T>> =
    if (isEmpty()) emptySequence()
    else lazyCartesianProductAcc(this, emptyList())
