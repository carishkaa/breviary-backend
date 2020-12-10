package blue.mild.breviary.backend.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import mu.KLogging

private const val NUMBER_OF_REPEATS = 4
private const val INTERVAL_MILLIS = 1_000L

object TestUtils : KLogging() {
    /**
     * Run lambda containing tested code until the provided condition si satisfied. In case that any exception is thrown in [testFunction], it is ignored until
     * the condition is satisfied, withing prescribed amount of repeats, [numberOfRepeats]. If condition is not satisfied after given number of repeats
     * [blue.mild.breviary.errors.InvalidStateBreviaryException] is thrown.
     *
     * @param T type of return value.
     * @param testFunction tested function
     * @param condition lambda testing result if it is true result is returned
     * @param message message to be output as a fail, in case [condition] is not satisfied
     * @param numberOfRepeats number of times the test function is called.
     * @param intervalMillis the amount of sleep time between repeats
     * @return result of function [testFunction]
     */
    suspend fun <T> repeatThenFailAsync(
        testFunction: () -> T,
        condition: (T) -> Boolean,
        message: () -> String,
        numberOfRepeats: Int = NUMBER_OF_REPEATS,
        intervalMillis: Long = INTERVAL_MILLIS
    ): T {
        repeat(numberOfRepeats) {
            logger.info("Testing condition in repeat $it.")
            val returnValue = testFunction()
            if (condition(returnValue)) {
                logger.info("Condition passed after $it repeats.")
                return returnValue
            }
            delay(intervalMillis)
        }

        error(
            ("${message()} Call `repeatThenFail` failed, passed condition was not satisfied after $numberOfRepeats repeats " +
                    "and ${numberOfRepeats * intervalMillis} millis.").trimIndent()
        )
    }

    /**
     * Run lambda containing tested code until the provided condition si satisfied. In case that any exception is thrown in [testFunction], it is ignored until
     * the condition is satisfied, withing prescribed amount of repeats, [numberOfRepeats]. If condition is not satisfied after given number of repeats
     * [blue.mild.breviary.errors.InvalidStateBreviaryException] is thrown.
     *
     * @param T type of return value.
     * @param testFunction tested function
     * @param condition lambda testing result if it is true result is returned
     * @param message message to be output as a fail, in case [condition] is not satisfied
     * @param numberOfRepeats number of times the test function is called.
     * @param intervalMillis the amount of sleep time between repeats
     * @return result of function [testFunction]
     */
    fun <T> repeatThenFail(
        testFunction: () -> T,
        condition: (T) -> Boolean,
        message: () -> String,
        numberOfRepeats: Int = NUMBER_OF_REPEATS,
        intervalMillis: Long = INTERVAL_MILLIS
    ): T = runBlocking { repeatThenFailAsync(testFunction, condition, message, numberOfRepeats, intervalMillis) }

    /**
     * Run lambda containing tested code until the provided condition si satisfied. In case that any exception is thrown in [condition], it is ignored until
     * the condition is satisfied, withing prescribed amount of repeats, [numberOfRepeats]. If condition is not satisfied after given number of repeats
     * [blue.mild.breviary.errors.InvalidStateBreviaryException] is thrown.
     *
     * @param condition condition to be tested
     * @param message message to be output as a fail, in case [condition] is not satisfied
     * @param numberOfRepeats number of times the test function is called.
     * @param intervalMillis the amount of sleep time between repeats
     * @return result of function [condition]
     */
    fun repeatThenFail(
        condition: () -> Boolean,
        message: () -> String,
        numberOfRepeats: Int = NUMBER_OF_REPEATS,
        intervalMillis: Long = INTERVAL_MILLIS
    ): Boolean = repeatThenFail(condition, { it }, message, numberOfRepeats, intervalMillis)
}
