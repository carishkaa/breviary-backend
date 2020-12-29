package blue.mild.breviary.backend.controllers

import blue.mild.breviary.backend.ApiRoutes
import blue.mild.breviary.backend.dtos.PasswordChangeDtoIn
import blue.mild.breviary.backend.services.AuthenticationService
import blue.mild.breviary.backend.services.PasswordService
import io.swagger.annotations.ApiOperation
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
 * @property passwordService [PasswordService]
 * @property authenticationService [PasswordService]
 */
@RestController
@RequestMapping("${ApiRoutes.BASE_PATH}/${ApiRoutes.PasswordChange.BASE}")
class PasswordChangeController(
    private val passwordService: PasswordService,
    private val authenticationService: AuthenticationService
) {
    /**
     * Changes user password.
     *
     * @param passwordChange [PasswordChangeDtoIn]
     * @return [ResponseEntity<Any>]
     */
    @ApiOperation("Changes user password.")
    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun changePassword(@Valid @RequestBody passwordChange: PasswordChangeDtoIn): ResponseEntity<Any> {
        val username = passwordService.changePassword(passwordChange)
        SecurityContextHolder.getContext().authentication = authenticationService.createdAuthenticationToken(username)
        return authenticationService.createResponseWithJsonWebTokenInHeaders(
            username,
            HttpStatus.OK
        )
    }
}
