package blue.mild.breviary.backend.services

import blue.mild.breviary.backend.db.repositories.UserRepository
import blue.mild.breviary.backend.db.repositories.extensions.findByUsernameOrThrow
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

/**
 * DBUserDetailsService.
 *
 * @property userRepository [UserRepository]
 * @property authenticationService [AuthenticationService]
 */
@Service("DBUserDetails")
class DbUserDetailsService(
    private val userRepository: UserRepository,
    private val authenticationService: AuthenticationService

) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsernameOrThrow(username)
        return User(user.username, user.password, authenticationService.getAuthorities(user.username))
    }
}
