package blue.mild.breviary.backend.db.repositories

import blue.mild.breviary.backend.db.entities.HeparinDosageEntity
import org.springframework.data.repository.CrudRepository

interface HeparinDosageRepository : CrudRepository<HeparinDosageEntity, Long>
