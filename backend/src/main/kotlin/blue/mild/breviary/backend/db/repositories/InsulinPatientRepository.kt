package blue.mild.breviary.backend.db.repositories

import blue.mild.breviary.backend.db.entities.InsulinPatientEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface InsulinPatientRepository : CrudRepository<InsulinPatientEntity, Long> {
    @Query("SELECT ip FROM InsulinPatientEntity ip WHERE ip.patient.active = true ORDER BY ip.id DESC")
    fun getActivePatients(): Collection<InsulinPatientEntity>
}
