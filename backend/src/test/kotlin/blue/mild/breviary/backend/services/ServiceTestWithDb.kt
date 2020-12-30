package blue.mild.breviary.backend.services

import Constants.TEST_PASSWORD
import Constants.TEST_USERNAME
import blue.mild.breviary.backend.BackendApplication
import blue.mild.breviary.backend.dtos.UserDtoIn
import blue.mild.breviary.backend.dtos.UserDtoOut
import blue.mild.breviary.backend.enums.UserRole
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(
    classes = [BackendApplication::class]
)
@ActiveProfiles("test")
@BaseServiceTestWithDb
open class ServiceTestWithDb {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    @Autowired
    lateinit var authenticationService: AuthenticationService

    @Autowired
    lateinit var flyway: Flyway

    @BeforeEach
    fun init() {
        flyway.clean()
        flyway.migrate()
    }

    /**
     * Helper test authentication.
     */
    protected fun createAndAuthenticateUser() {
        val user = userService.addUser(
            UserDtoIn(
                email = TEST_USERNAME,
                password = TEST_PASSWORD
            ),
            true
        )
        userService.setUserRoles(user.id, listOf(UserRole.NURSE, UserRole.DOCTOR, UserRole.ADMINISTRATOR))
        authenticateUser()
    }

    protected fun authenticateUser() {
        SecurityContextHolder.getContext().authentication =
            authenticationService.createdAuthenticationToken(TEST_USERNAME)
    }

    protected fun createUser(): UserDtoOut {
        return userService.addUser(
            user = UserDtoIn(TEST_USERNAME, TEST_PASSWORD),
            active = true
        )
    }
}
