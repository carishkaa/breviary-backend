package blue.mild.breviary.backend.controllers

import blue.mild.breviary.backend.ApiRoutes
import blue.mild.breviary.backend.dtos.UserSignupDtoIn
import blue.mild.breviary.backend.services.AuthenticationService
import blue.mild.breviary.backend.services.UserService
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
 * SignupController.
 *
 * @property UserService
 * @property authenticationService
 */
@RestController
@RequestMapping("${ApiRoutes.BASE_PATH}/${ApiRoutes.SIGNUP}")
class SignupController(
    private val userService: UserService,
    private val authenticationService: AuthenticationService
) {
    /**
     * Sign up new user.
     *
     * @param user
     * @return
     */
    @Suppress("ReturnCount")
    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun signup(@Valid @RequestBody user: UserSignupDtoIn): ResponseEntity<Any> {
        val newUser = userService.signUpUser(user)
        SecurityContextHolder.getContext().authentication =
            authenticationService.createdAuthenticationToken(newUser.email)

        return authenticationService.createResponseWithJsonWebTokenInHeaders(newUser.email, HttpStatus.CREATED)
    }
}
