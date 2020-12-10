package blue.mild.breviary.backend.db.repositories

import blue.mild.breviary.backend.db.entities.UserEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface UserRepository : CrudRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.roles WHERE u.id =:id")
    override fun findById(id: Long): Optional<UserEntity>

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.roles WHERE LOWER(u.username) LIKE LOWER(:username) AND u.deleted IS NULL")
    fun findByUsername(username: String): UserEntity?

    @Query("SELECT u.username FROM UserEntity u WHERE u.deleted IS NULL")
    fun getUsernames(): Iterable<String>
}
