@file:Suppress("TooManyFunctions")

package blue.mild.breviary.backend.db.repositories.extensions

import blue.mild.breviary.backend.db.entities.BaseEntity
import blue.mild.breviary.backend.db.entities.UserEntity
import blue.mild.breviary.backend.db.repositories.UserRepository
import blue.mild.breviary.backend.errors.EntityNotFoundBreviaryException
import org.springframework.data.repository.CrudRepository

/**
 * Find the entity with a given ID or throw an exception if it does not exist.
 *
 * @param id The ID of the requested entity.
 * @return the found entity.
 * @throws blue.mild.breviary.backend.errors.EntityNotFoundBreviaryException The entity with the given id was not present in the database.
 */
inline fun <reified T, ID> CrudRepository<T, ID>.findByIdOrThrow(id: ID): T =
    findById(id).orElseThrow { EntityNotFoundBreviaryException("${T::class.simpleName} with id $id not found.") }

/**
 * Maps deleted entities to null.
 */
fun <T : BaseEntity> BaseEntity.mapDeletedToNull(): T? {
    return if (this.deleted == null) this as T?
    else null
}

/**
 * Find user by username or throw an exception.
 *
 * @param username The username to be found.
 * @return the found user.
 * @throws blue.mild.breviary.backend.errors.EntityNotFoundBreviaryException The user with the given username is not present in the database.
 */
fun UserRepository.findByUsernameOrThrow(username: String): UserEntity =
    findByUsername(username)
        ?.mapDeletedToNull<UserEntity>()
        ?: throw EntityNotFoundBreviaryException("User with email '$username' not found.")

/**
 * Find user by id or throw an exception.
 *
 * @param userId The user id to be found.
 * @return the found user.
 * @throws blue.mild.breviary.backend.errors.EntityNotFoundBreviaryException The user with the given username is not present in the database.
 */
fun UserRepository.findByIdOrThrow(userId: Long): UserEntity {
    val user = findById(userId)

    if (user.isEmpty || user.get().deleted != null) {
        throw EntityNotFoundBreviaryException("User with id '$userId' not found.")
    }
    return user.get()
}
