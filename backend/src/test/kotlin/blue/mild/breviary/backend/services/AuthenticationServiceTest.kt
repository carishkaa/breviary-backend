package blue.mild.breviary.backend.services

import Constants.TEST_USERNAME
import blue.mild.breviary.backend.errors.UnauthenticatedBreviaryException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AuthenticationServiceTest : ServiceTestWithDb() {

    @Test
    fun `should get authentication from context or throw exception when not present`() {
        assertThrows<UnauthenticatedBreviaryException> { authenticationService.getAuthentication() }

        createAndAuthenticateUser()
        val authentication = authenticationService.getAuthentication()
        assertNotNull(authentication)
    }

    @Test
    fun `should get user entity or throw exception when not present`() {
        assertThrows<UnauthenticatedBreviaryException> { authenticationService.getUser() }

        createAndAuthenticateUser()
        val user = authenticationService.getAuthentication()
        assertNotNull(user)
        assertEquals(TEST_USERNAME, user.name)
    }

    @Test
    fun `should get username or throw exception when not present`() {
        assertThrows<UnauthenticatedBreviaryException> { authenticationService.getUsername() }

        createAndAuthenticateUser()
        val username = authenticationService.getUsername()
        assertEquals(TEST_USERNAME, username)
    }

    @Test
    fun `should get user roles or throw exception when not present`() {
        assertThrows<UnauthenticatedBreviaryException> { authenticationService.getRoles() }

        createAndAuthenticateUser()
        val userRoles = authenticationService.getRoles()
        assertNotNull(userRoles)
        assertEquals(3, userRoles.size)
    }
}
