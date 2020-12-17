package blue.mild.breviary.backend.db.repositories

import blue.mild.breviary.backend.db.entities.InsulinDosageEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.time.Instant

interface InsulinDosageRepository : CrudRepository<InsulinDosageEntity, Long> {
    fun getByInsulinPatientId(insulinPatientId: Long): Collection<InsulinDosageEntity>

    @Query("SELECT ido " +
        "FROM InsulinDosageEntity ido " +
        "WHERE ido.insulinPatient.id = :insulinPatientId AND ido.created >= :datetime " +
        "ORDER BY ido.id DESC")
    fun getDosagesAppliedAfterDatetime(insulinPatientId: Long, datetime: Instant): Collection<InsulinDosageEntity>
}
