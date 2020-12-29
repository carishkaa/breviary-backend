package blue.mild.breviary.backend.errors

/**
 * CannotDeleteEntityBreviaryException exception.
 */
class CannotDeleteEntityBreviaryException(message: String, causes: List<Throwable> = listOf()) :
    DataBreviaryException(message, causes)
