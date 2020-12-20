package blue.mild.breviary.backend.extensions

import blue.mild.breviary.backend.db.entities.UserEntity
import blue.mild.breviary.backend.dtos.UserDtoOut
import pw.forst.tools.katlib.mapToSet

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
