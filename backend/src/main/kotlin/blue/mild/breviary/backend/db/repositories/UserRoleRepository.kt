package blue.mild.breviary.backend.db.repositories

import blue.mild.breviary.backend.db.entities.UserRoleEntity
import org.springframework.data.repository.CrudRepository

interface UserRoleRepository : CrudRepository<UserRoleEntity, Long>
