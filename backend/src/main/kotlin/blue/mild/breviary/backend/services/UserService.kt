package blue.mild.breviary.backend.services

import ai.blindspot.ktoolz.extensions.mapToSet
import blue.mild.breviary.backend.EMPTY_PASSWORD
import blue.mild.breviary.backend.PropertiesNames
import blue.mild.breviary.backend.db.entities.UserEntity
import blue.mild.breviary.backend.db.entities.UserRoleEntity
import blue.mild.breviary.backend.db.repositories.PasswordResetRepository
import blue.mild.breviary.backend.db.repositories.RoleRepository
import blue.mild.breviary.backend.db.repositories.UserRepository
import blue.mild.breviary.backend.db.repositories.UserRoleRepository
import blue.mild.breviary.backend.db.repositories.extensions.findByIdOrThrow
import blue.mild.breviary.backend.db.repositories.extensions.findByUsernameOrThrow
import blue.mild.breviary.backend.dtos.PayloadDto
import blue.mild.breviary.backend.dtos.UserDtoIn
import blue.mild.breviary.backend.dtos.UserDtoOut
import blue.mild.breviary.backend.dtos.UserSignupDtoIn
import blue.mild.breviary.backend.enums.UserRole
import blue.mild.breviary.backend.errors.EntityAlreadyExistsBreviaryException
import blue.mild.breviary.backend.errors.EntityNotFoundBreviaryException
import blue.mild.breviary.backend.errors.InvalidArgumentBreviaryException
import blue.mild.breviary.backend.extensions.toDtoOut
import blue.mild.breviary.backend.utils.generateRandomString
import blue.mild.breviary.backend.utils.isEmail
import blue.mild.breviary.backend.utils.isNotNullOrEmpty
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * UserService.
 *
 * @property userRepository
 * @property userRoleRepository
 * @property roleRepository
 * @property bCryptPasswordEncoder
 * @property passwordResetRepository
 */
@Suppress("TooManyFunctions")
@Service
class UserService(
    private val userRepository: UserRepository,
    private val userRoleRepository: UserRoleRepository,
    private val roleRepository: RoleRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val passwordResetRepository: PasswordResetRepository
) {

    /**
     * Get user by name.
     *
     * @param username
     * @return
     */
    fun getUserByUsername(username: String): UserDtoOut {
        val userEntity = userRepository.findByUsernameOrThrow(username)

        return userEntity.toDtoOut()
    }

    /**
     * Get user by id.
     *
     * @param userId
     * @return
     */
    fun getUserById(userId: Long): UserDtoOut {
        val userEntity = userRepository.findByIdOrThrow(userId)
        return userEntity.toDtoOut()
    }

    /**
     * Checks if user exists.
     *
     * @param username
     * @return
     */
    fun userExists(username: String): Boolean = userRepository.findByUsername(username) != null

    /**
     * Add user.
     * If the `active` flag is set to `false`, validation is skipped and `unactive` user is created.
     *
     * @param user
     * @param active
     * @return
     */
    @Transactional
    @Throws(
        EntityAlreadyExistsBreviaryException::class,
        InvalidArgumentBreviaryException::class,
        EntityNotFoundBreviaryException::class
    )
    fun addUser(user: UserDtoIn, active: Boolean): UserDtoOut {
        if (active) {
            validateUsername(user.email)
        }

        val userEntity = userRepository.save(
            UserEntity(
                username = user.email.toLowerCase(),
                password = bCryptPasswordEncoder.encode(user.password),
                active = active
            )
        )
        return userEntity.toDtoOut()
    }

    /**
     * Sign up a new user with worker.
     *
     * @param user
     * @return
     */
    @Transactional
    @Throws(EntityAlreadyExistsBreviaryException::class)
    fun signUpUser(user: UserSignupDtoIn): UserDtoOut {
        val newUser = addUser(
            UserDtoIn(
                email = user.email,
                password = user.password
            ),
            true
        )
        val userRoles = setUserRoles(newUser.id, listOf())
        return newUser.copy(roles = userRoles.mapToSet { it.role.name })
    }

    /**
     * Sets user roles.
     *
     * @param userId
     * @param roles
     */
    @Transactional
    @Throws(EntityNotFoundBreviaryException::class)
    fun setUserRoles(userId: Long, roles: List<UserRole>): List<UserRoleEntity> {
        val userEntity = userRepository.findByIdOrThrow(userId)
        val stayUserRoles = userEntity.roles.filter { roles.contains(it.role.name) }
        val newlyAddedRoles = roleRepository.findAllByName(roles.subtract(stayUserRoles.map { it.role.name }))
        val newUserRoles = stayUserRoles + userRoleRepository.saveAll(newlyAddedRoles.map {
            UserRoleEntity(
                role = it,
                user = userEntity
            )
        })
        val userRolesToDelete = userEntity.roles.minus(newUserRoles)
        userRoleRepository.deleteAll(userRolesToDelete)
        return newUserRoles
    }

    /**
     * Update user.
     *
     * @param userId
     * @param user
     * @return
     */
    @Transactional
    @Throws(EntityNotFoundBreviaryException::class)
    fun updateUser(userId: Long, user: UserDtoIn): UserDtoOut {
        val userEntity = userRepository.findByIdOrThrow(userId)
        val updatedUserEntity = userEntity.copy(password = bCryptPasswordEncoder.encode(user.password))
        userRepository.save(updatedUserEntity)
        return updatedUserEntity.toDtoOut()
    }

    /**
     * Update names of the user.
     *
     * @param userId
     * @param user
     * @return
     */
    @Transactional
    @Throws(EntityNotFoundBreviaryException::class, EntityAlreadyExistsBreviaryException::class)
    fun updateUserNames(userId: Long, user: UserDtoIn): UserDtoOut {
        val userEntity = userRepository.findByIdOrThrow(userId)
        if (isNotNullOrEmpty(user.email) &&
            userEntity.username != user.email &&
            userRepository.findByUsername(user.email) != null
        ) {
            throw EntityAlreadyExistsBreviaryException(
                "User with email '${user.email}' already exists.",
                payload = PayloadDto(hashMapOf(PropertiesNames.EMAIL to "User with this email already exists."))
            )
        }
        val updatedUserEntity = userEntity.copy(username = user.email)
        userRepository.save(updatedUserEntity)
        return updatedUserEntity.toDtoOut()
    }

    /**
     * Activates user.
     *
     * @param userId
     * @param username
     * @return
     */
    @Transactional
    @Throws(InvalidArgumentBreviaryException::class, EntityNotFoundBreviaryException::class)
    fun activateUser(userId: Long, username: String): UserDtoOut {
        validateUsername(username, userId)

        val userEntity = userRepository.findByIdOrThrow(userId)
        val updatedUserEntity = userEntity.copy(
            username = username,
            password = bCryptPasswordEncoder.encode(generateRandomString()),
            active = true
        )
        userRepository.save(updatedUserEntity)
        return updatedUserEntity.toDtoOut()
    }

    /**
     * Deactivates user.
     *
     * @param userId
     * @return
     */
    @Transactional
    @Throws(EntityNotFoundBreviaryException::class)
    fun deactivateUser(userId: Long): UserDtoOut {
        val userEntity = userRepository.findByIdOrThrow(userId)
        val updatedUserEntity = userEntity.copy(password = EMPTY_PASSWORD, active = false)
        userRepository.save(updatedUserEntity)
        return updatedUserEntity.toDtoOut()
    }

    /**
     * Delete user.
     *
     * @param userId
     */
    @Transactional
    @Throws(EntityNotFoundBreviaryException::class)
    fun deleteUser(userId: Long) {
        val userEntity = userRepository.findByIdOrThrow(userId)

        passwordResetRepository.deleteByUserId(userEntity.id)
        userRepository.deleteById(userEntity.id)
    }

    @Suppress("ThrowsCount")
    @Throws(
        InvalidArgumentBreviaryException::class,
        EntityAlreadyExistsBreviaryException::class
    )
    private fun validateUsername(username: String, userId: Long? = null) {
        if (!username.isEmail()) {
            throw InvalidArgumentBreviaryException(
                "Email '$username' must be valid.",
                payload = PayloadDto(hashMapOf(PropertiesNames.EMAIL to "Email must be valid."))
            )
        }

        val existingUserWithName = userRepository.findByUsername(username)
        if (existingUserWithName != null && existingUserWithName.deleted == null) {
            if (userId == null) {
                throw EntityAlreadyExistsBreviaryException(
                    "User with email '$username' already exists.",
                    payload = PayloadDto(hashMapOf(PropertiesNames.EMAIL to "User with this email already exists."))
                )
            } else {
                val userById = userRepository.findByIdOrThrow(userId)
                if (isNotNullOrEmpty(userById.username) && userById.username != username) {
                    throw EntityAlreadyExistsBreviaryException(
                        "User with email '$username' already exists.",
                        payload = PayloadDto(hashMapOf(PropertiesNames.EMAIL to "User with this email already exists."))
                    )
                }
            }
        }
    }
}
