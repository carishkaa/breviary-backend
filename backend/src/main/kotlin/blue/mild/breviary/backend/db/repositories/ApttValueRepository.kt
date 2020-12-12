package blue.mild.breviary.backend.db.repositories

import blue.mild.breviary.backend.db.entities.ApttValueEntity
import org.springframework.data.repository.CrudRepository

interface ApttValueRepository : CrudRepository<ApttValueEntity, Long>
