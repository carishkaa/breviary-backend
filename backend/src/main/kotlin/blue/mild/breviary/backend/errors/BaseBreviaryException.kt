package blue.mild.breviary.backend.errors

/**
 * Base exception.
 * Exception thrown when `retry` fails - contains the exceptions which made the retry fail.
 */
open class BaseBreviaryException(message: String, causes: List<Throwable>) :
    RuntimeException(message, if (causes.any()) causes.last() else null)
