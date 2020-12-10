package blue.mild.breviary.backend.errors

/**
 * Base data exception.
 */
open class DataBreviaryException(message: String, causes: List<Throwable>) : BaseBreviaryException(message, causes)
