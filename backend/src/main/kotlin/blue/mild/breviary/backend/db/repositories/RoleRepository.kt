package blue.mild.breviary.backend.db.repositories

import blue.mild.breviary.backend.db.entities.RoleEntity
import blue.mild.breviary.backend.enums.UserRole
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface RoleRepository : CrudRepository<RoleEntity, Long> {

    @Query("SELECT re FROM RoleEntity re WHERE re.name IN :names")
    fun findAllByName(names: Set<UserRole>): Iterable<RoleEntity>
}
