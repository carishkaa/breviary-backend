package blue.mild.breviary.backend.controllers

import Constants.TEST_PASSWORD
import Constants.TEST_USERNAME
import blue.mild.breviary.backend.ApiRoutes
import blue.mild.breviary.backend.dtos.PasswordChangeDtoIn
import blue.mild.breviary.backend.dtos.ResponseDto
import blue.mild.breviary.backend.dtos.UserDtoOut
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import kotlin.test.assertEquals

class PasswordChangeControllerTest : ControllerTest() {

    @Test
    @DirtiesContext
    fun `should change password`() {
        signup()
        authenticateUser()

        val newPassword = "23523k5h23ih6"

        val result = executeClientPost(
            "$apiPrefix/${ApiRoutes.PasswordChange.BASE}",
            setOf(HttpStatus.OK),
            typeReference<UserDtoOut>(),
            HttpEntity<Any>(
                PasswordChangeDtoIn(
                    currentPassword = TEST_PASSWORD,
                    newPassword = newPassword,
                    newPasswordAgain = newPassword
                ),
                getAuthHeaders()
            )
        )
        assertEquals(TEST_USERNAME, result.body!!.email)
    }

    @Test
    @DirtiesContext
    fun `should not change password because of invalid current password`() {
        signup()
        authenticateUser()

        val newPassword = "23523k5h23ih6"

        executeClientPost(
            "$apiPrefix/${ApiRoutes.PasswordChange.BASE}",
            setOf(HttpStatus.BAD_REQUEST),
            typeReference<ResponseDto>(),
            HttpEntity<Any>(
                PasswordChangeDtoIn(
                    currentPassword = "BAD PASSWORD",
                    newPassword = newPassword,
                    newPasswordAgain = newPassword
                ),
                getAuthHeaders()
            )
        )
    }

    @Test
    @DirtiesContext
    fun `should not change password because of invalid new password and its validation`() {
        signup()
        authenticateUser()

        val newPassword = "23523k5h23ih6"

        executeClientPost(
            "$apiPrefix/${ApiRoutes.PasswordChange.BASE}",
            setOf(HttpStatus.BAD_REQUEST),
            typeReference<ResponseDto>(),
            HttpEntity<Any>(
                PasswordChangeDtoIn(
                    currentPassword = TEST_PASSWORD,
                    newPassword = newPassword,
                    newPasswordAgain = "BAD PASSWORD"
                ),
                getAuthHeaders()
            )
        )
    }
}
