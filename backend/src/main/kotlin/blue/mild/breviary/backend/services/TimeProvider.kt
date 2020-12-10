package blue.mild.breviary.backend.services

import java.time.temporal.Temporal

/**
 * Class providing access to current time via [now] method.
 *
 * This class is needed in order to test methods that require usage of current time stamp.
 */
interface TimeProvider<T : Temporal> {

    /**
     * Returns value representing current time stamp as defined in the implementation of [T].
     */
    fun now(): T
}
