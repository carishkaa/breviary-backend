package blue.mild.breviary.backend.db.repositories

import blue.mild.breviary.backend.db.entities.HeparinPatientEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface HeparinPatientRepository : CrudRepository<HeparinPatientEntity, Long> {
    @Query("SELECT hp FROM HeparinPatientEntity hp WHERE hp.patient.active = true ORDER BY hp.id DESC")
    fun getActivePatients(): Collection<HeparinPatientEntity>
}
