package blue.mild.breviary.backend.errors

/**
 * Wrapper over Kotlin check function to be able to provide custom exception.
 *
 * @param value
 * @param lazyMessage
 * @throws blue.mild.breviary.backend.errors.InvalidStateBreviaryException
 */
inline fun checkBreviary(value: Boolean, lazyMessage: () -> Any) {
    try {
        return check(value, lazyMessage)
    } catch (isex: IllegalStateException) {
        throw InvalidStateBreviaryException(isex.message ?: "Invalid state", listOf(isex))
    }
}

/**
 * Wrapper over Kotlin require function to be able to provide custom exception.
 *
 * @param value
 * @param lazyMessage
 * @throws blue.mild.breviary.backend.errors.InvalidArgumentBreviaryException
 */
inline fun requireBreviary(value: Boolean, lazyMessage: () -> Any) {
    try {
        return require(value, lazyMessage)
    } catch (iaex: IllegalArgumentException) {
        throw InvalidArgumentBreviaryException(iaex.message ?: "Invalid argument", listOf(iaex))
    }
}

/**
 * Wrapper over Kotlin requireNotNull function to be able to provide custom exception.
 *
 * @param T
 * @param value
 * @param lazyMessage
 * @return
 */
inline fun <T : Any> requireNotNullBreviary(value: T?, lazyMessage: () -> Any): T {
    try {
        return requireNotNull(value, lazyMessage)
    } catch (iaex: IllegalArgumentException) {
        throw InvalidArgumentBreviaryException(iaex.message ?: "Invalid argument", listOf(iaex))
    }
}
