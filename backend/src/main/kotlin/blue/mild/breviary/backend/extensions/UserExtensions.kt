package blue.mild.breviary.backend.extensions

import ai.blindspot.ktoolz.extensions.mapToSet
import blue.mild.breviary.backend.db.entities.UserEntity
import blue.mild.breviary.backend.dtos.UserDtoOut

/**
 * UserEntity to UserDtoOut.
 *
 * @return
 */
fun UserEntity.toDtoOut(): UserDtoOut =
    UserDtoOut(
        id = id,
        email = username,
        password = password,
        roles = roles.mapToSet { it.role.name },
        active = active
    )
