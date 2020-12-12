package blue.mild.breviary.backend.db.repositories

import blue.mild.breviary.backend.db.entities.PatientEntity
import org.springframework.data.repository.CrudRepository

interface PatientRepository : CrudRepository<PatientEntity, Long>
