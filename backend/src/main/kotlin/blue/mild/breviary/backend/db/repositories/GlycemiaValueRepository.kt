package blue.mild.breviary.backend.db.repositories

import blue.mild.breviary.backend.db.entities.GlycemiaValueEntity
import org.springframework.data.repository.CrudRepository

interface GlycemiaValueRepository : CrudRepository<GlycemiaValueEntity, Long>
