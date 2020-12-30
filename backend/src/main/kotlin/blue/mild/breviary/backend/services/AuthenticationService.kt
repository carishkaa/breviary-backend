package blue.mild.breviary.backend.services

import blue.mild.breviary.backend.config.WebSecurityConfig
import blue.mild.breviary.backend.db.entities.UserEntity
import blue.mild.breviary.backend.db.repositories.UserRepository
import blue.mild.breviary.backend.db.repositories.extensions.findByUsernameOrThrow
import blue.mild.breviary.backend.dtos.UserDtoOut
import blue.mild.breviary.backend.enums.UserRole
import blue.mild.breviary.backend.errors.UnauthenticatedBreviaryException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import pw.forst.tools.katlib.mapToSet

/**
 * AuthenticationService.
 *
 * @property userRepository [UserRepository]
 */
@Service
@Suppress("TooManyFunctions")
class AuthenticationService(
    private val userRepository: UserRepository
) {

    /**
     * Gets authentication.
     *
     * @return
     */
    fun getAuthentication(): Authentication =
        SecurityContextHolder.getContext().authentication
            ?: throw UnauthenticatedBreviaryException("No authentication found.")

    /**
     * Gets user.
     * Should be called in transactional scope when adding to another entity.
     *
     * @return
     */
    fun getUser(): UserEntity = userRepository.findByUsernameOrThrow(getUsername())

    /**
     * Gets username.
     *
     * @return
     */
    fun getUsername(): String = getAuthentication().name

    /**
     * Gets roles.
     *
     */
    fun getRoles() = getAuthentication().authorities.map { UserRole.valueOf(it.authority) }

    /**
     * Function to create authentication token.
     *
     * @param principal or "username"
     */
    fun createdAuthenticationToken(principal: String) =
        UsernamePasswordAuthenticationToken(principal, null, getAuthorities(principal))

    /**
     * Get user's authorities from user's roles.
     *
     * @param username
     * @return
     */
    fun getAuthorities(username: String): List<GrantedAuthority> =
        getUserRoles(username).map { SimpleGrantedAuthority(it.name) }

    /**
     * Check if user is a scheduler.
     *
     * @return
     */
    fun isDoctor(): Boolean = getRoles().contains(UserRole.DOCTOR)

    /**
     * Check if user is an administrator.
     *
     * @return
     */
    fun isAdmin(): Boolean = getRoles().contains(UserRole.ADMINISTRATOR)

    /**
     * Creates response with JWT in headers.
     *
     * @param username
     * @return
     */
    fun createResponseWithJsonWebTokenInHeaders(username: String, responseStatus: HttpStatus): ResponseEntity<Any> =
        ResponseEntity(
            UserDtoOut(
                email = username,
                roles = getUserRoles(username),
            ),
            JWTAuthenticationFilter.createHeadersWithJsonWebToken(username, WebSecurityConfig.SET_COOKIE_KEY),
            responseStatus
        )

    private fun getUserRoles(username: String): Set<UserRole> =
        userRepository.findByUsernameOrThrow(username).roles.mapToSet { it.role.name }
}
