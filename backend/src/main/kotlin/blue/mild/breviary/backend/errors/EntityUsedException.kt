package blue.mild.breviary.backend.errors

/**
 * EntityUsedException.
 *
 * @property message
 */
class EntityUsedException(override val message: String) : Exception(message)
