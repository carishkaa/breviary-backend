package blue.mild.breviary.backend.controllers

import blue.mild.breviary.backend.ApiRoutes.ERROR
import blue.mild.breviary.backend.config.handlers.GlobalErrorHandler
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpServletRequest

/**
 * Whitelabel Error Page Controller.
 *
 */
@Controller
class CustomErrorController : ErrorController {
    /**
     * Handles error page.
     *
     * @param request
     * @return
     */
    @Suppress("ReturnCount")
    @RequestMapping("/$ERROR", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun handleError(request: HttpServletRequest): ResponseEntity<Any> {
        val status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)
        if (status != null) {
            val statusCode = Integer.valueOf(status.toString())
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return GlobalErrorHandler.getErrorResponse(Exception(), HttpStatus.NOT_FOUND, "Resource not found.")
            }
            if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return GlobalErrorHandler.getErrorResponse(
                    Exception(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Internal server error."
                )
            }
        }
        return GlobalErrorHandler.getErrorResponse(
            Exception(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Internal server error."
        )
    }

    /**
     * Gets error path.
     *
     * @return
     */
    override fun getErrorPath(): String = "/$ERROR"
}
