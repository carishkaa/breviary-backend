package blue.mild.breviary.backend.config.handlers

import blue.mild.breviary.backend.dtos.PayloadDto
import blue.mild.breviary.backend.dtos.ResponseDto
import blue.mild.breviary.backend.errors.CannotDeleteEntityBreviaryException
import blue.mild.breviary.backend.errors.EntityAlreadyExistsBreviaryException
import blue.mild.breviary.backend.errors.EntityNotFoundBreviaryException
import blue.mild.breviary.backend.errors.FormFieldErrorInput
import blue.mild.breviary.backend.errors.InvalidArgumentBreviaryException
import blue.mild.breviary.backend.errors.InvalidStateBreviaryException
import blue.mild.breviary.backend.errors.SecurityBreviaryException
import blue.mild.breviary.backend.errors.UnauthenticatedBreviaryException
import mu.KLogging
import org.hibernate.exception.ConstraintViolationException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import pw.forst.tools.katlib.createJson
import java.lang.reflect.UndeclaredThrowableException

/**
 * Global REST error handler for returning better error response messages.
 */
@Suppress("ComplexMethod", "TooManyFunctions")
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class GlobalErrorHandler {

    companion object : KLogging() {
        /**
         * Created logger response.
         *
         * @param ex
         * @param statusCode
         * @param message
         * @return
         */
        fun getErrorResponse(ex: Exception, statusCode: HttpStatus, message: String = ""): ResponseEntity<Any> {
            val headers = HttpHeaders()
                .apply { set("Content-Type", MediaType.APPLICATION_JSON.toString()) }

            val payload = when (ex) {
                is FormFieldErrorInput -> ex.payload
                else -> PayloadDto()
            }

            return ResponseEntity(
                createJson(ResponseDto(ex.message ?: message, statusCode.value(), payload = payload)),
                headers,
                statusCode
            )
        }
    }

    /**
     * Handle and process InvalidArgumentBreviaryException, IllegalArgumentException, EntityAlreadyExistsBreviaryException and other client related errors.
     *
     * @param ex Exception
     * @return Error response
     */
    @ExceptionHandler(
        InvalidArgumentBreviaryException::class,
        IllegalArgumentException::class,
        CannotDeleteEntityBreviaryException::class
    )
    fun badRequestHandler(ex: Exception): ResponseEntity<Any> =
        getErrorResponse(ex, HttpStatus.BAD_REQUEST, "Bad request.")
            .also {
                logger.error(ex) { "${HttpStatus.BAD_REQUEST} returned." }
            }

    /**
     * Handle and process EntityNotFoundBreviaryException.
     *
     * @param ex Exception
     * @return Error response
     */
    @ExceptionHandler(EntityNotFoundBreviaryException::class)
    fun notFoundHandler(ex: Exception): ResponseEntity<Any> =
        getErrorResponse(ex, HttpStatus.NOT_FOUND, "Resource not found.")
            .also {
                logger.error(ex) { "${HttpStatus.NOT_FOUND} returned." }
            }

    /**
     * Handle and process InvalidStateBreviaryException and DataIntegrityViolationException.
     *
     * @param ex Exception
     * @return Error response
     */
    @ExceptionHandler(
        InvalidStateBreviaryException::class,
        DataIntegrityViolationException::class,
        EntityAlreadyExistsBreviaryException::class
    )
    fun handleIntegrityViolationException(ex: Exception) = getErrorResponse(
        ex,
        HttpStatus.CONFLICT,
        "Resource is in conflict with the state of the system. Most probably, a resource with the same name already exists."
    ).also { logger.error(ex) { "${HttpStatus.CONFLICT} returned." } }

    /**
     * Handle and process AccessDeniedException and AccessDeniedBreviaryException.
     *
     * @param ex Exception
     * @return Error response
     */
    @ExceptionHandler(AccessDeniedException::class, SecurityBreviaryException::class)
    fun accessDeniedHandler(ex: Exception): ResponseEntity<Any> = getErrorResponse(ex, HttpStatus.FORBIDDEN)
        .also { logger.error(ex) { "${HttpStatus.FORBIDDEN} returned." } }

    /**
     * Handle and process ConstraintViolationException.
     *
     * @param ex Exception
     * @return Error response
     */
    @ExceptionHandler(ConstraintViolationException::class)
    fun accessConstraintViolationHandler(ex: Exception): ResponseEntity<Any> =
        getErrorResponse(ex, HttpStatus.BAD_REQUEST)
            .also { logger.error(ex) { "${HttpStatus.BAD_REQUEST} returned." } }

    /**
     * Handle and process HttpMediaTypeNotSupportedException.
     *
     * @param ex Exception
     * @return Error response
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    fun badMediaTypeHandler(ex: Exception): ResponseEntity<Any> =
        getErrorResponse(
            ex,
            HttpStatus.UNSUPPORTED_MEDIA_TYPE,
            "Only '${MediaType.APPLICATION_JSON}' content type is supported."
        ).also { logger.error(ex) { "${HttpStatus.UNSUPPORTED_MEDIA_TYPE} returned." } }

    /**
     * Handle and process BadCredentialsException and UnauthenticatedBreviaryException.
     *
     * @param ex Exception
     * @return Error response
     */
    @ExceptionHandler(BadCredentialsException::class, UnauthenticatedBreviaryException::class)
    fun unauthorizedHandler(ex: Exception): ResponseEntity<Any> = getErrorResponse(ex, HttpStatus.UNAUTHORIZED)
        .also { logger.error(ex) { "${HttpStatus.UNAUTHORIZED} returned." } }

    /**
     * Handle and process HttpRequestMethodNotSupportedException.
     *
     * @param ex Exception
     * @return Error response
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun unsupportedMethodHandler(ex: Exception): ResponseEntity<Any> = getErrorResponse(ex, HttpStatus.BAD_REQUEST)
        .also { logger.error(ex) { "${HttpStatus.BAD_REQUEST} returned." } }

    /**
     * Handle and process HttpRequestMethodNotSupportedException.
     *
     * @param ex Exception
     * @return Error response
     */
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun notReadableExceptionHandler(ex: Exception): ResponseEntity<Any> = getErrorResponse(ex, HttpStatus.BAD_REQUEST)
        .also { logger.error(ex) { "${HttpStatus.BAD_REQUEST} returned." } }

    /**
     * Handle and process Exception.
     *
     * @param ex Exception
     * @return Error response
     */
    @ExceptionHandler(Exception::class)
    fun otherErrorHandler(ex: Exception): ResponseEntity<Any> =
        getErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error. Please, contact support.")
            .also { logger.error(ex) { "${HttpStatus.INTERNAL_SERVER_ERROR} returned." } }

    /**
     * Handle and process Exception.
     *
     * @param ex Exception
     * @return Error response
     */
    @ExceptionHandler(UndeclaredThrowableException::class)
    fun undeclaredThrowableHandler(ex: Exception): ResponseEntity<Any> =
        when (val cause = ex.cause) {
            is InvalidArgumentBreviaryException -> badRequestHandler(cause)
            is IllegalArgumentException -> badRequestHandler(cause)
            is CannotDeleteEntityBreviaryException -> badRequestHandler(cause)
            is EntityNotFoundBreviaryException -> notFoundHandler(cause)
            is InvalidStateBreviaryException -> handleIntegrityViolationException(cause)
            is DataIntegrityViolationException -> handleIntegrityViolationException(cause)
            is EntityAlreadyExistsBreviaryException -> handleIntegrityViolationException(cause)
            is AccessDeniedException -> accessDeniedHandler(cause)
            is SecurityBreviaryException -> accessDeniedHandler(cause)
            is ConstraintViolationException -> accessConstraintViolationHandler(cause)
            is HttpMediaTypeNotSupportedException -> badMediaTypeHandler(cause)
            is BadCredentialsException -> unauthorizedHandler(cause)
            is UnauthenticatedBreviaryException -> unauthorizedHandler(cause)
            is HttpRequestMethodNotSupportedException -> unsupportedMethodHandler(cause)
            is HttpMessageNotReadableException -> notReadableExceptionHandler(cause)
            else -> otherErrorHandler((cause ?: Exception("Unknown cause exception.")) as Exception)
        }
}
