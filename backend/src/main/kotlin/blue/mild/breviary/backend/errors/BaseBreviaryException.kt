package blue.mild.breviary.backend.errors

/**
 * Base exception.
 * Exception thrown when `retry` fails - contains the exceptions which made the retry fail.
 */
open class BaseBreviaryException(message: String, causes: List<Throwable>) :
    Exception(message, if (causes.any()) causes.last() else Exception("No base exception."))
