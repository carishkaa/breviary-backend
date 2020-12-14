package blue.mild.breviary.backend.controllers

import blue.mild.breviary.backend.ApiRoutes
import blue.mild.breviary.backend.PropertiesNames
import blue.mild.breviary.backend.config.handlers.GlobalErrorHandler.Companion.getErrorResponse
import blue.mild.breviary.backend.dtos.LoginDtoIn
import blue.mild.breviary.backend.dtos.PayloadDto
import blue.mild.breviary.backend.errors.InvalidArgumentBreviaryException
import blue.mild.breviary.backend.services.AuthenticationService
import blue.mild.breviary.backend.services.UserService
import blue.mild.breviary.backend.utils.isNullOrEmpty
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * LoginController.
 *
 * @property usersService [UserService]
 * @property bCryptPasswordEncoder [BCryptPasswordEncoder]
 * @property authenticationService [AuthenticationService]
 */
@RestController
@RequestMapping("${ApiRoutes.BASE_PATH}/${ApiRoutes.LOGIN}")
class LoginController(
    private val usersService: UserService,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val authenticationService: AuthenticationService
) {
    /**
     * User login.
     *
     * @param user [LoginDtoIn]
     * @return [ResponseEntity<Any>]
     */
    @Suppress("ReturnCount")
    @ApiOperation("Login user into the application.")
    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun login(@Valid @RequestBody user: LoginDtoIn): ResponseEntity<Any> {
        if (isNullOrEmpty(user.email)) {
            return getErrorResponse(
                InvalidArgumentBreviaryException(
                    "Email must not be empty.",
                    payload = PayloadDto(hashMapOf(PropertiesNames.EMAIL to "Email must not be empty."))
                ),
                HttpStatus.BAD_REQUEST
            )
        }

        if (!usersService.userExists(user.email)) {
            return getErrorResponse(
                InvalidArgumentBreviaryException(
                    "Email '${user.email}' not found.",
                    payload = PayloadDto(hashMapOf(PropertiesNames.EMAIL to "Email not found."))
                ),
                HttpStatus.NOT_FOUND
            )
        }

        val dbUser = usersService.getUserByUsername(user.email)
        if (!bCryptPasswordEncoder.matches(user.password, dbUser.password)) {
            return getErrorResponse(
                InvalidArgumentBreviaryException(
                    "Invalid password.",
                    payload = PayloadDto(hashMapOf(PropertiesNames.PASSWORD to "Invalid password."))
                ),
                HttpStatus.UNAUTHORIZED
            )
        }

        SecurityContextHolder.getContext().authentication = authenticationService.createdAuthenticationToken(user.email)
        return authenticationService.createResponseWithJsonWebTokenInHeaders(user.email, HttpStatus.OK)
    }
}
