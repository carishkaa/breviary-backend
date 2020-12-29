package blue.mild.breviary.backend.services

import Constants.TEST_PASSWORD
import Constants.TEST_USERNAME
import blue.mild.breviary.backend.EMPTY_PASSWORD
import blue.mild.breviary.backend.EMPTY_USERNAME
import blue.mild.breviary.backend.dtos.UserDtoIn
import blue.mild.breviary.backend.enums.UserRole
import blue.mild.breviary.backend.errors.EntityAlreadyExistsBreviaryException
import blue.mild.breviary.backend.errors.EntityNotFoundBreviaryException
import blue.mild.breviary.backend.errors.InvalidArgumentBreviaryException
import blue.mild.breviary.backend.utils.isNullOrEmpty
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UserServiceTest : ServiceTestWithDb() {
    @Test
    fun `should set user roles`() {
        val createdUser = createUser()

        assertTrue(createdUser.roles.isEmpty())
        val userRoles = listOf(UserRole.ADMINISTRATOR, UserRole.NURSE)
        userService.setUserRoles(createdUser.id, userRoles)
        val rolesToCheck = userService.getUserById(createdUser.id).roles
        assertTrue(rolesToCheck.contains(UserRole.ADMINISTRATOR))
        assertTrue(rolesToCheck.contains(UserRole.NURSE))
        assertFalse(rolesToCheck.contains(UserRole.DOCTOR))
    }

    @Suppress("LongMethod")
    @Test
    fun `should process service CRUD operations`() {
        val newPassword = "dfsdfsgsligjsog"
        val nonExistingUsername = "non-existing@example.org"

        val createdUser = createUser()

        assertEquals(TEST_USERNAME, createdUser.email)
        assertTrue(bCryptPasswordEncoder.matches(TEST_PASSWORD, createdUser.password))
        assertTrue(createdUser.roles.minus(setOf()).isEmpty())

        // Get existing user by name
        val getExistingUserByName = userService.getUserByUsername(createdUser.email)
        assertEquals(createdUser.email, getExistingUserByName.email)
        assertEquals(createdUser.password, getExistingUserByName.password)
        assertTrue(createdUser.roles.minus(getExistingUserByName.roles).isEmpty())

        // Get existing user by id
        val getExistingUserById = userService.getUserById(createdUser.id)
        assertEquals(createdUser.email, getExistingUserById.email)
        assertEquals(createdUser.password, getExistingUserById.password)
        assertTrue(createdUser.roles.minus(getExistingUserById.roles).isEmpty())

        // Try get non-existing user by id
        assertFailsWith<EntityNotFoundBreviaryException> {
            userService.getUserById(666)
        }

        // Insert duplicate user
        assertFailsWith<EntityAlreadyExistsBreviaryException> {
            userService.addUser(
                user = UserDtoIn(
                    TEST_USERNAME,
                    TEST_PASSWORD
                ),
                active = true
            )
        }

        // Insert user with invalid username
        assertFailsWith<InvalidArgumentBreviaryException> {
            userService.addUser(
                user = UserDtoIn(
                    "invalid-username",
                    TEST_PASSWORD
                ),
                active = true
            )
        }

        // Insert user2
        val username2 = "user2@example.org"
        var createdUser2 = userService.addUser(
            UserDtoIn(username2, TEST_PASSWORD),
            true
        )

        authenticateUser()
        createdUser2 = userService.getUserByUsername(createdUser2.email)

        assertEquals(username2, createdUser2.email)
        assertTrue(bCryptPasswordEncoder.matches(TEST_PASSWORD, createdUser2.password))
        assertTrue(createdUser.roles.minus(setOf()).isEmpty())

        // Insert user2 again
        assertFailsWith<EntityAlreadyExistsBreviaryException> {
            userService.addUser(
                user = UserDtoIn(
                    "user2@example.org",
                    TEST_PASSWORD
                ),
                active = true
            )
        }

        // Get non-existing user by name
        assertFailsWith<EntityNotFoundBreviaryException> { userService.getUserByUsername(nonExistingUsername) }

        // Check if user with name exists
        assertTrue(userService.userExists(createdUser.email))
        assertFalse(userService.userExists(nonExistingUsername))

        // Update user
        val updatedUser = userService.updateUser(
            createdUser.id,
            UserDtoIn(TEST_USERNAME, newPassword)
        )
        assertEquals(createdUser.email, updatedUser.email)
        assertTrue(bCryptPasswordEncoder.matches(newPassword, updatedUser.password))
    }

    @Test
    fun `should create user, deactivate and active it back`() {
        val createdUser1 = userService.addUser(
            user = UserDtoIn(TEST_USERNAME, TEST_PASSWORD),
            active = true
        )

        val createdUser2 = userService.addUser(
            user = UserDtoIn("$TEST_USERNAME-2", TEST_PASSWORD),
            active = true
        )

        assertEquals(TEST_USERNAME, createdUser1.email)
        assertTrue(bCryptPasswordEncoder.matches(TEST_PASSWORD, createdUser1.password))
        assertTrue(createdUser1.roles.minus(setOf()).isEmpty())
        assertTrue(createdUser1.active)

        // Deactivate user1
        val inactiveUser1 = userService.deactivateUser(createdUser1.id)
        assertEquals(EMPTY_PASSWORD, inactiveUser1.password)
        assertFalse(inactiveUser1.active)

        // Deactivate user2 to verify DB constrain
        val inactiveUser2 = userService.deactivateUser(createdUser2.id)
        assertEquals(EMPTY_PASSWORD, inactiveUser2.password)
        assertFalse(inactiveUser2.active)

        // Try to activate with non-existing user id
        assertFailsWith<EntityNotFoundBreviaryException> {
            userService.activateUser(666, TEST_USERNAME)
        }

        // Activate user1 back
        val activeUser = userService.activateUser(createdUser1.id, TEST_USERNAME)
        assertEquals(TEST_USERNAME, activeUser.email)
        assertTrue(!isNullOrEmpty(activeUser.password))
        assertTrue(activeUser.active)

        // Activate user again
        val activeUser2 = userService.activateUser(createdUser1.id, TEST_USERNAME)
        assertEquals(TEST_USERNAME, activeUser2.email)
        assertTrue(!isNullOrEmpty(activeUser2.password))
        assertTrue(activeUser2.active)

        // Create inactive user
        val createdUser3 = userService.addUser(
            UserDtoIn(EMPTY_USERNAME, EMPTY_PASSWORD),
            false
        )
        assertEquals(EMPTY_USERNAME, createdUser3.email)
        assertTrue(!isNullOrEmpty(createdUser3.password))
        assertFalse(createdUser3.active)
    }

    @Suppress("LongMethod")
    @Test
    fun `should create user and update its names`() {
        val createdUser1 = userService.addUser(
            user = UserDtoIn(TEST_USERNAME, TEST_PASSWORD),
            active = true
        )

        userService.addUser(
            user = UserDtoIn("$TEST_USERNAME-2", TEST_PASSWORD),
            active = true
        )

        assertEquals(TEST_USERNAME, createdUser1.email)
        assertTrue(bCryptPasswordEncoder.matches(TEST_PASSWORD, createdUser1.password))
        assertTrue(createdUser1.roles.minus(setOf()).isEmpty())
        assertTrue(createdUser1.active)

        // Change names of user 1
        userService.updateUserNames(
            createdUser1.id,
            UserDtoIn(
                "$TEST_USERNAME-1",
                EMPTY_PASSWORD
            )
        )

        // Change names of user 1 again as previous
        userService.updateUserNames(
            createdUser1.id,
            UserDtoIn(
                "$TEST_USERNAME-1",
                EMPTY_PASSWORD
            )
        )

        // Change username of user 1 back
        userService.updateUserNames(
            createdUser1.id,
            UserDtoIn(
                TEST_USERNAME,
                EMPTY_PASSWORD
            )
        )

        // Change username of user 1 to EMPTY_USERNAME
        userService.updateUserNames(
            createdUser1.id,
            UserDtoIn(
                EMPTY_USERNAME,
                EMPTY_PASSWORD
            )
        )

        // Try to change it to existing username
        assertFailsWith<EntityAlreadyExistsBreviaryException> {
            userService.updateUserNames(
                createdUser1.id,
                UserDtoIn(
                    "$TEST_USERNAME-2",
                    EMPTY_PASSWORD
                )
            )
        }
    }
}
