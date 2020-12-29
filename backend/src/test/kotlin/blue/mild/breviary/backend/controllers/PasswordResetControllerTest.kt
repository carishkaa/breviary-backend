package blue.mild.breviary.backend.controllers

import Constants.TEST_PASSWORD
import Constants.TEST_USERNAME
import blue.mild.breviary.backend.ApiRoutes
import blue.mild.breviary.backend.db.repositories.PasswordResetRepository
import blue.mild.breviary.backend.db.repositories.UserRepository
import blue.mild.breviary.backend.dtos.PasswordResetDtoIn
import blue.mild.breviary.backend.dtos.PasswordResetRequestDtoIn
import blue.mild.breviary.backend.dtos.PasswordResetRequestDtoOut
import blue.mild.breviary.backend.dtos.ResponseDto
import blue.mild.breviary.backend.dtos.UserDtoIn
import blue.mild.breviary.backend.dtos.UserDtoOut
import blue.mild.breviary.backend.services.PasswordService
import blue.mild.breviary.backend.services.UserService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import kotlin.test.assertEquals

class PasswordResetControllerTest(
    @Autowired private val passwordService: PasswordService,
    @Autowired private val passwordResetRepository: PasswordResetRepository,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val userService: UserService
) : ControllerTest() {

    @Test
    @DirtiesContext
    fun `should reset password`() {
        userService.addUser(UserDtoIn(TEST_USERNAME, TEST_PASSWORD), true)

        // Create password reset request first
        val requestResult = executeClientPost(
            "$apiPrefix/${ApiRoutes.PasswordReset.BASE}/${ApiRoutes.PasswordReset.REQUEST}",
            setOf(HttpStatus.OK),
            typeReference<PasswordResetRequestDtoOut>(),
            HttpEntity<Any>(
                PasswordResetRequestDtoIn(
                    username = TEST_USERNAME
                )
            )
        )
        assertEquals("Password reset request has been created.", requestResult.body!!.message)

        passwordService.createPasswordResetRequest(TEST_USERNAME)
        val user = userRepository.findByUsername(TEST_USERNAME)!!
        val request = passwordResetRepository.findByUserId(user.id).first()
        val newPassword = "23523k5h23ih6"

        val result = executeClientPost(
            "$apiPrefix/${ApiRoutes.PasswordReset.BASE}",
            setOf(HttpStatus.OK),
            typeReference<UserDtoOut>(),
            HttpEntity<Any>(
                PasswordResetDtoIn(
                    token = request.token,
                    newPassword = newPassword
                )
            )
        )
        assertEquals(TEST_USERNAME, result.body!!.email)
    }

    @Test
    @DirtiesContext
    fun `should not reset password because of missing request`() {
        userService.addUser(UserDtoIn(TEST_USERNAME, TEST_PASSWORD), true)

        val newPassword = "23523k5h23ih6"

        executeClientPost(
            "$apiPrefix/${ApiRoutes.PasswordReset.BASE}",
            setOf(HttpStatus.NOT_FOUND),
            typeReference<ResponseDto>(),
            HttpEntity<Any>(
                PasswordResetDtoIn(
                    token = "some token",
                    newPassword = newPassword
                )
            )
        )
    }

    @Test
    @DirtiesContext
    fun `should not reset password because of missing username`() {
        executeClientPost(
            "$apiPrefix/${ApiRoutes.PasswordReset.BASE}/${ApiRoutes.PasswordReset.REQUEST}",
            setOf(HttpStatus.NOT_FOUND),
            typeReference<PasswordResetRequestDtoOut>(),
            HttpEntity<Any>(
                PasswordResetRequestDtoIn(
                    username = TEST_USERNAME
                )
            )
        )
    }
}
