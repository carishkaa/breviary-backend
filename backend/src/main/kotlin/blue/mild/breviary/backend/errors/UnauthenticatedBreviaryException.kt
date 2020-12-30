package blue.mild.breviary.backend.errors

/**
 * UnauthenticatedBreviaryException exception.
 */
open class UnauthenticatedBreviaryException(message: String, causes: List<Throwable> = listOf()) :
    SecurityBreviaryException(message, causes)
