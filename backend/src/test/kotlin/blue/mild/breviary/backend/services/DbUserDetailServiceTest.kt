package blue.mild.breviary.backend.services

import Constants.TEST_PASSWORD
import Constants.TEST_USERNAME
import blue.mild.breviary.backend.errors.EntityNotFoundBreviaryException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DbUserDetailServiceTest(@Autowired private val dbUserDetailsService: DbUserDetailsService) : ServiceTestWithDb() {

    @Test
    fun `should load user details from DB or throw exception`() {
        assertThrows<EntityNotFoundBreviaryException> { dbUserDetailsService.loadUserByUsername(TEST_USERNAME) }

        createAndAuthenticateUser()
        val userDetails = dbUserDetailsService.loadUserByUsername(TEST_USERNAME)
        assertNotNull(userDetails)
        assertEquals(TEST_USERNAME, userDetails.username)
        assertTrue(bCryptPasswordEncoder.matches(TEST_PASSWORD, userDetails.password))
    }
}
