package blue.mild.breviary.backend.db.repositories

import blue.mild.breviary.backend.db.entities.PasswordResetEntity
import org.springframework.data.repository.CrudRepository

interface PasswordResetRepository : CrudRepository<PasswordResetEntity, Long> {
    fun findByToken(token: String): PasswordResetEntity?

    fun findByUserId(userId: Long): List<PasswordResetEntity>

    fun deleteByUserId(userId: Long)
}
