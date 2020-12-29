package blue.mild.breviary.backend.services

import blue.mild.breviary.backend.PropertiesNames
import blue.mild.breviary.backend.db.entities.PasswordResetEntity
import blue.mild.breviary.backend.db.repositories.PasswordResetRepository
import blue.mild.breviary.backend.db.repositories.UserRepository
import blue.mild.breviary.backend.db.repositories.extensions.findByUsernameOrThrow
import blue.mild.breviary.backend.dtos.PasswordChangeDtoIn
import blue.mild.breviary.backend.dtos.PasswordResetDtoIn
import blue.mild.breviary.backend.dtos.PayloadDto
import blue.mild.breviary.backend.errors.EntityNotFoundBreviaryException
import blue.mild.breviary.backend.errors.InvalidArgumentBreviaryException
import mu.KLogging
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.transaction.Transactional

/**
 * Service to reset user password.
 *
 * @property authenticationService
 * @property instantTimeProvider
 * @property resetPasswordResetRepository
 * @property userRepository
 * @property bCryptPasswordEncoder
 */
@Service
class PasswordService(
    private val authenticationService: AuthenticationService,
    private val instantTimeProvider: InstantTimeProvider,
    private val resetPasswordResetRepository: PasswordResetRepository,
    private val userRepository: UserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) {

    companion object : KLogging() {
        const val TOKEN_VALIDATION_IN_HOURS = 24L
        const val MIN_PASSWORD_LENGTH = 5
    }

    /**
     * Resets user password.
     *
     * @param passwordReset
     * @return
     */
    @Transactional
    fun resetPassword(passwordReset: PasswordResetDtoIn): String {
        val resetPasswordEntity =
            resetPasswordResetRepository.findByToken(passwordReset.token)
                ?: throw EntityNotFoundBreviaryException("Could not find password reset token ${passwordReset.token}.")

        val now = instantTimeProvider.now()
        if (now != resetPasswordEntity.validUntil && now.isAfter(resetPasswordEntity.validUntil)) {
            resetPasswordResetRepository.delete(resetPasswordEntity)
            throw InvalidArgumentBreviaryException("Password reset token ${passwordReset.token} already expired.")
        }

        validatePasswordComplexity(passwordReset.newPassword)

        val user = userRepository.findByUsernameOrThrow(resetPasswordEntity.user.username)
        val updatedUser = user.copy(password = bCryptPasswordEncoder.encode(passwordReset.newPassword))

        userRepository.save(updatedUser)
        resetPasswordResetRepository.delete(resetPasswordEntity)

        return user.username
    }

    /**
     * Change user password.
     *
     * @param passwordChange
     * @return
     */
    @Transactional
    fun changePassword(passwordChange: PasswordChangeDtoIn): String {
        val currentUser = authenticationService.getUser()

        if (passwordChange.newPassword != passwordChange.newPasswordAgain) {
            throw InvalidArgumentBreviaryException(
                "New password and its confirmation do not match.",
                payload = PayloadDto(hashMapOf(PropertiesNames.NEW_PASSWORD to "New password and its confirmation do not match."))
            )
        }

        validatePasswordComplexity(passwordChange.newPassword)

        if (!bCryptPasswordEncoder.matches(passwordChange.currentPassword, currentUser.password)) {
            throw InvalidArgumentBreviaryException(
                "Current password does not match.",
                payload = PayloadDto(hashMapOf(PropertiesNames.PASSWORD to "Current password does not match."))
            )
        }

        val user = userRepository.findByUsernameOrThrow(currentUser.username)
        val updatedUser = user.copy(password = bCryptPasswordEncoder.encode(passwordChange.newPassword))
        userRepository.save(updatedUser)

        return user.username
    }

    /**
     * Creates new password reset request for user (and removes previous requests).
     *
     * @param username
     */
    @Transactional
    fun createPasswordResetRequest(username: String) {
        val user = userRepository.findByUsernameOrThrow(username)
        val alreadyExistingRequests = resetPasswordResetRepository.findByUserId(user.id)

        if (alreadyExistingRequests.any()) {
            logger.warn { "Removing existing reset password requests for user with email ${user.username}." }
            resetPasswordResetRepository.deleteAll(alreadyExistingRequests)
        }
        // TODO generate crypto-secure tokens, don't use uuid
        val token = UUID.randomUUID().toString()
        resetPasswordResetRepository.save(
            PasswordResetEntity(
                token = token,
                validUntil = instantTimeProvider.now().plus(TOKEN_VALIDATION_IN_HOURS, ChronoUnit.HOURS),
                user = user
            )
        )

        // TODO: Send some information to user.
    }

    private fun validatePasswordComplexity(password: String) {
        if (password.length < MIN_PASSWORD_LENGTH) {
            throw InvalidArgumentBreviaryException(
                "Password must have at least $MIN_PASSWORD_LENGTH characters.",
                payload = PayloadDto(hashMapOf(PropertiesNames.PASSWORD to "Password must have at least $MIN_PASSWORD_LENGTH characters."))
            )
        }
    }
}
