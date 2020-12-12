package blue.mild.breviary.backend.db.repositories

import blue.mild.breviary.backend.db.entities.InsulinDosageEntity
import org.springframework.data.repository.CrudRepository

interface InsulinDosageRepository : CrudRepository<InsulinDosageEntity, Long>
