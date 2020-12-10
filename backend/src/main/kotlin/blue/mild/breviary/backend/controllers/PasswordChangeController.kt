package blue.mild.breviary.backend.controllers

import blue.mild.breviary.backend.ApiRoutes
import blue.mild.breviary.backend.dtos.PasswordChangeDtoIn
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
 * PasswordChangeController.
 *
 * @property passwordService
 */
@RestController
@RequestMapping("${ApiRoutes.BASE_PATH}/${ApiRoutes.PasswordChange.BASE}")
class PasswordChangeController(
    private val passwordService: PasswordService,
    private val authenticationService: AuthenticationService
) {
    /**
     * Change user password.
     *
     * @param passwordChange
     * @return
     */
    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun change(@Valid @RequestBody passwordChange: PasswordChangeDtoIn): ResponseEntity<Any> {
        val username = passwordService.changePassword(passwordChange)
        SecurityContextHolder.getContext().authentication = authenticationService.createdAuthenticationToken(username)
        return authenticationService.createResponseWithJsonWebTokenInHeaders(
            username,
            HttpStatus.OK
        )
    }
}
