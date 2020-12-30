package blue.mild.breviary.backend.errors

/**
 * Security exception.
 */
open class SecurityBreviaryException(message: String, causes: List<Throwable>) : BaseBreviaryException(message, causes)
