package blue.mild.breviary.backend.controllers

import blue.mild.breviary.backend.ApiRoutes
import blue.mild.breviary.backend.dtos.PasswordResetDtoIn
import blue.mild.breviary.backend.dtos.PasswordResetRequestDtoIn
import blue.mild.breviary.backend.dtos.PasswordResetRequestDtoOut
import blue.mild.breviary.backend.services.AuthenticationService
import blue.mild.breviary.backend.services.PasswordService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * PasswordResetController.
 *
 * @property passwordService
 */
@RestController
@RequestMapping("${ApiRoutes.BASE_PATH}/${ApiRoutes.PasswordReset.BASE}")
class PasswordResetController(
    private val passwordService: PasswordService,
    private val authenticationService: AuthenticationService
) {
    /**
     * Resets user password.
     *
     * @param passwordReset
     * @return
     */
    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun reset(@Valid @RequestBody passwordReset: PasswordResetDtoIn): ResponseEntity<Any> {
        val username = passwordService.resetPassword(passwordReset)
        SecurityContextHolder.getContext().authentication = authenticationService.createdAuthenticationToken(username)
        return authenticationService.createResponseWithJsonWebTokenInHeaders(
            username,
            HttpStatus.OK
        )
    }

    /**
     * Creates user password request.
     *
     * @param passwordResetRequest
     * @return
     */
    @PostMapping(ApiRoutes.PasswordReset.REQUEST, produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createPasswordResetRequest(@Valid @RequestBody passwordResetRequest: PasswordResetRequestDtoIn): PasswordResetRequestDtoOut {
        passwordService.createPasswordResetRequest(passwordResetRequest.username)
        return PasswordResetRequestDtoOut("Password reset request has been created.")
    }
}
