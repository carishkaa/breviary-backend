package blue.mild.breviary.backend.controllers

import Constants.TEST_PASSWORD
import Constants.TEST_USERNAME
import blue.mild.breviary.backend.ApiRoutes
import blue.mild.breviary.backend.EMPTY_PASSWORD
import blue.mild.breviary.backend.EMPTY_USERNAME
import blue.mild.breviary.backend.dtos.LoginDtoIn
import blue.mild.breviary.backend.dtos.ResponseDto
import blue.mild.breviary.backend.dtos.UserDtoOut
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LoginControllerTest : ControllerTest() {

    @Test
    @DirtiesContext
    fun `should log in user`() {
        signup()

        // Log in
        val loginResult = executeClientPost(
            "$apiPrefix/${ApiRoutes.LOGIN}",
            setOf(HttpStatus.OK),
            typeReference<UserDtoOut>(),
            HttpEntity<Any>(
                LoginDtoIn(
                    email = TEST_USERNAME,
                    password = TEST_PASSWORD
                ),
                getAuthHeaders()
            )
        )
        assertNotNull(loginResult.body)
        assertEquals(TEST_USERNAME, loginResult.body!!.email)
        assertEquals(setOf(), loginResult.body!!.roles)

        // Login with invalid password
        executeClientPost(
            "$apiPrefix/${ApiRoutes.LOGIN}",
            setOf(HttpStatus.UNAUTHORIZED),
            typeReference<ResponseDto>(),
            HttpEntity<Any>(
                LoginDtoIn(
                    email = TEST_USERNAME,
                    password = "INVALID"
                ),
                getAuthHeaders()
            )
        )
    }

    @Test
    @DirtiesContext
    fun `should not log in and return UNAUTHORIZED when user is not signed up`() {
        // Log in
        executeClientPost(
            "$apiPrefix/${ApiRoutes.LOGIN}",
            setOf(HttpStatus.UNAUTHORIZED),
            typeReference<ResponseDto>(),
            HttpEntity<Any>(
                LoginDtoIn(
                    email = TEST_USERNAME,
                    password = TEST_PASSWORD
                ),
                getAuthHeaders()
            )
        )
    }

    @Test
    @DirtiesContext
    fun `should not log in and return NOT_FOUND when authentication header is missing`() {
        // Log in
        executeClientPost(
            "$apiPrefix/${ApiRoutes.LOGIN}",
            setOf(HttpStatus.NOT_FOUND),
            typeReference<ResponseDto>(),
            HttpEntity<Any>(
                LoginDtoIn(
                    email = TEST_USERNAME,
                    password = TEST_PASSWORD
                )
            )
        )
    }

    @Test
    @DirtiesContext
    fun `should not log in and return BAD_REQUEST when username is empty`() {
        signup()

        // Empty username
        executeClientPost(
            "$apiPrefix/${ApiRoutes.LOGIN}",
            setOf(HttpStatus.BAD_REQUEST),
            typeReference<ResponseDto>(),
            HttpEntity<Any>(
                LoginDtoIn(
                    email = EMPTY_USERNAME,
                    password = EMPTY_PASSWORD
                ),
                getAuthHeaders()
            )
        )
    }
}
