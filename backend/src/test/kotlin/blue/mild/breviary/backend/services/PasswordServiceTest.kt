package blue.mild.breviary.backend.services

import Constants.TEST_PASSWORD
import Constants.TEST_USERNAME
import blue.mild.breviary.backend.db.repositories.PasswordResetRepository
import blue.mild.breviary.backend.dtos.PasswordChangeDtoIn
import blue.mild.breviary.backend.dtos.PasswordResetDtoIn
import blue.mild.breviary.backend.errors.EntityNotFoundBreviaryException
import blue.mild.breviary.backend.errors.InvalidArgumentBreviaryException
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.temporal.ChronoUnit
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PasswordServiceTest(
    @Autowired private val passwordService: PasswordService,
    @Autowired private val passwordResetRepository: PasswordResetRepository
) : ServiceTestWithDb() {

    @Test
    fun `should reset password successfully`() {
        createAndAuthenticateUser()

        val user = userService.getUserByUsername(TEST_USERNAME)

        // Create request first
        passwordService.createPasswordResetRequest(user.email)
        val request = passwordResetRepository.findByUserId(user.id).first()
        val newPassword = "23523k5h23ih6"

        // Reset password
        val username = passwordService.resetPassword(
            PasswordResetDtoIn(
                token = request.token,
                newPassword = newPassword
            )
        )
        assertEquals(TEST_USERNAME, username)

        val userRequests = passwordResetRepository.findByUserId(user.id)
        assertFalse(userRequests.any())

        val updatedUser = userService.getUserByUsername(TEST_USERNAME)
        assertTrue(bCryptPasswordEncoder.matches(newPassword, updatedUser.password))
    }

    @Test
    fun `should not reset password because reset request does not exist`() {
        createAndAuthenticateUser()

        val newPassword = "23523k5h23ih6"

        assertFailsWith<EntityNotFoundBreviaryException> {
            // Reset password
            passwordService.resetPassword(
                PasswordResetDtoIn(
                    token = "different token",
                    newPassword = newPassword
                )
            )
        }
    }

    @Test
    fun `should not reset password because request token expired`() {
        createAndAuthenticateUser()

        val user = userService.getUserByUsername(TEST_USERNAME)

        // Create request first
        passwordService.createPasswordResetRequest(user.email)
        val request = passwordResetRepository.findByUserId(user.id).first()
        val invalidDate = request.validUntil.minus(PasswordService.TOKEN_VALIDATION_IN_HOURS, ChronoUnit.HOURS)
        val updatedRequest = request.copy(validUntil = invalidDate)
        passwordResetRepository.save(updatedRequest)
        val newPassword = "23523k5h23ih6"

        assertFailsWith<InvalidArgumentBreviaryException> {
            // Reset password
            passwordService.resetPassword(
                PasswordResetDtoIn(
                    token = request.token,
                    newPassword = newPassword
                )
            )
        }

        val userRequests = passwordResetRepository.findByUserId(user.id)
        assertFalse(userRequests.any())
    }

    @Test
    fun `should not reset password because of password complexity`() {
        createAndAuthenticateUser()

        val user = userService.getUserByUsername(TEST_USERNAME)

        // Create request first
        passwordService.createPasswordResetRequest(user.email)
        val request = passwordResetRepository.findByUserId(user.id).first()
        val newPassword = "aaa"

        assertFailsWith<InvalidArgumentBreviaryException> {
            // Reset password
            passwordService.resetPassword(
                PasswordResetDtoIn(
                    token = request.token,
                    newPassword = newPassword
                )
            )
        }
    }

    @Test
    fun `multiple request creations should remove all previous`() {
        createAndAuthenticateUser()

        val user = userService.getUserByUsername(TEST_USERNAME)

        // Create request for user
        passwordService.createPasswordResetRequest(user.email)
        var requests = passwordResetRepository.findByUserId(user.id)
        assertEquals(1, requests.size)

        // Create second request for user
        passwordService.createPasswordResetRequest(user.email)
        requests = passwordResetRepository.findByUserId(user.id)
        assertEquals(1, requests.size)
    }

    @Test
    fun `should change password successfully`() {
        createAndAuthenticateUser()

        val newPassword = "23523k5h23ih6"

        // Change password
        val username = passwordService.changePassword(
            PasswordChangeDtoIn(
                currentPassword = TEST_PASSWORD,
                newPassword = newPassword,
                newPasswordAgain = newPassword
            )
        )
        assertEquals(TEST_USERNAME, username)

        val updatedUser = userService.getUserByUsername(TEST_USERNAME)
        assertTrue(bCryptPasswordEncoder.matches(newPassword, updatedUser.password))
    }

    @Test
    fun `should not change password because invalid current password`() {
        createAndAuthenticateUser()

        val newPassword = "23523k5h23ih6"

        assertFailsWith<InvalidArgumentBreviaryException> {
            // Change password
            passwordService.changePassword(
                PasswordChangeDtoIn(
                    currentPassword = "BAD PASSWORD",
                    newPassword = newPassword,
                    newPasswordAgain = newPassword
                )
            )
        }
    }

    @Test
    fun `should not change password because new password and its validation do not match`() {
        createAndAuthenticateUser()

        val newPassword = "23523k5h23ih6"

        assertFailsWith<InvalidArgumentBreviaryException> {
            // Change password
            passwordService.changePassword(
                PasswordChangeDtoIn(
                    currentPassword = TEST_PASSWORD,
                    newPassword = newPassword,
                    newPasswordAgain = "BAD PASSWORD"
                )
            )
        }
    }

    @Test
    fun `should not change password because of password complexity`() {
        createAndAuthenticateUser()

        val newPassword = "aaa"

        assertFailsWith<InvalidArgumentBreviaryException> {
            // Change password
            passwordService.changePassword(
                PasswordChangeDtoIn(
                    currentPassword = TEST_PASSWORD,
                    newPassword = newPassword,
                    newPasswordAgain = newPassword
                )
            )
        }
    }
}
