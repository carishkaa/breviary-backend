package blue.mild.breviary.backend.db.repositories

import blue.mild.breviary.backend.db.entities.InsulinPatientEntity
import org.springframework.data.repository.CrudRepository

interface InsulinPatientRepository : CrudRepository<InsulinPatientEntity, Long>
