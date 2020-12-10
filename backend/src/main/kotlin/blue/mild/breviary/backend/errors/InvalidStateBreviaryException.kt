package blue.mild.breviary.backend.errors

/**
 * InvalidState exception.
 */
class InvalidStateBreviaryException(message: String, causes: List<Throwable> = listOf()) :
    DataBreviaryException(message, causes)
