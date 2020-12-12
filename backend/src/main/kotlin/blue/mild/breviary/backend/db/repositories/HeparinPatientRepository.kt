package blue.mild.breviary.backend.db.repositories

import blue.mild.breviary.backend.db.entities.HeparinPatientEntity
import org.springframework.data.repository.CrudRepository

interface HeparinPatientRepository : CrudRepository<HeparinPatientEntity, Long>
