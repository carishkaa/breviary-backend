package blue.mild.breviary.backend.controllers

import Constants.TEST_FIRST_NAME
import Constants.TEST_LAST_NAME
import Constants.TEST_PASSWORD
import Constants.TEST_USERNAME
import blue.mild.breviary.backend.ApiRoutes
import blue.mild.breviary.backend.dtos.ResponseDto
import blue.mild.breviary.backend.dtos.UserSignupDtoIn
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SignupControllerTest : ControllerTest() {

    @Test
    @DirtiesContext
    fun `should sign up new user`() {
        val validResult = signup()

        assertNotNull(validResult.body)
        assertEquals(TEST_USERNAME, validResult.body!!.email)
        assertEquals(setOf(), validResult.body!!.roles)

        // Re-signup should fail
        executeClientPost(
            "$apiPrefix/${ApiRoutes.SIGNUP}",
            setOf(HttpStatus.CONFLICT),
            typeReference<ResponseDto>(),
            HttpEntity<Any>(
                UserSignupDtoIn(
                    email = TEST_USERNAME,
                    password = TEST_PASSWORD,
                    firstName = TEST_FIRST_NAME,
                    lastName = TEST_LAST_NAME
                )
            )
        )
    }
}
