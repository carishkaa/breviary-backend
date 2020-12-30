package blue.mild.breviary.backend.utils

/**
 * Group sum.
 *
 * @param T
 * @param K
 * @param keySelector
 * @param valueTransform
 */
fun <T, K> Iterable<T>.groupSum(keySelector: (T) -> K, valueTransform: (T) -> Int) =
    this.groupBy(keySelector, valueTransform)
        .mapValues { it.value.sum() }

/**
 * Sum counts.
 *
 * @param K
 * @param first
 * @param second
 */
fun <K> sumCounts(first: Iterable<Pair<K, Int>>, second: Iterable<Pair<K, Int>>) =
    (first + second).groupSum({ it.first }, { it.second })

/**
 * Split new and old items.
 *
 * @param NEW
 * @param OLD
 * @param KEY
 * @param newItems
 * @param oldItems
 * @param newKeyMapper
 * @param oldKeyMapper
 * @return
 */
fun <NEW, OLD, KEY> splitNewOld(
    newItems: Collection<NEW>,
    oldItems: Collection<OLD>,
    newKeyMapper: (NEW) -> KEY,
    oldKeyMapper: (OLD) -> KEY
): Pair<List<NEW>, List<OLD>> {
    val newKeysMap = newItems.associateBy { newKeyMapper(it) }

    val new = mutableListOf<NEW>()
    val old = mutableListOf<OLD>()
    val oldKeys = mutableSetOf<KEY>()

    oldItems.forEach {
        val key = oldKeyMapper(it)
        oldKeys.add(key)
        if (!newKeysMap.containsKey(key)) {
            old.add(it)
        }
    }
    newKeysMap.forEach { (key, item) ->
        if (key !in oldKeys) {
            new.add(item)
        }
    }

    return Pair(new, old)
}
