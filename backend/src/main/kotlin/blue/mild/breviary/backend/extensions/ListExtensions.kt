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

// TODO move this to katlib
/**
 * Zip alternative for three collections.
 */
@Suppress("MagicNumber") // this is default implementation from the Kotlin collections
inline fun <A, B, C, V> Iterable<A>.zip(b: Iterable<B>, c: Iterable<C>, transform: (a: A, b: B, c: C) -> V): List<V> {
    val first = iterator()
    val second = b.iterator()
    val third = c.iterator()

    val list = ArrayList<V>(minOf(collectionSizeOrDefault(10), b.collectionSizeOrDefault(10), c.collectionSizeOrDefault(10)))
    while (first.hasNext() && second.hasNext() && third.hasNext()) {
        list.add(transform(first.next(), second.next(), third.next()))
    }

    return list
}

@PublishedApi
internal fun <T> Iterable<T>.collectionSizeOrDefault(default: Int): Int = if (this is Collection<*>) this.size else default
