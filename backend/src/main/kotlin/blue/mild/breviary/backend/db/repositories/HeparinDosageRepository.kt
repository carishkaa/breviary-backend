package blue.mild.breviary.backend.db.repositories

import blue.mild.breviary.backend.db.entities.HeparinDosageEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface HeparinDosageRepository : CrudRepository<HeparinDosageEntity, Long> {
    fun getByHeparinPatientId(heparinPatientId: Long): Collection<HeparinDosageEntity>

    @Query(
        value = "SELECT hd.* " +
            "FROM heparin_dosages hd " +
            "WHERE hd.heparin_patient_id = :heparinPatientId " +
            "ORDER BY hd.created DESC LIMIT 1",
        nativeQuery = true
    )
    fun getNewestByHeparinPatientId(heparinPatientId: Long): HeparinDosageEntity?

    @Query(
        value = "SELECT hd.* " +
            "FROM heparin_dosages hd " +
            "WHERE hd.heparin_patient_id = :heparinPatientId " +
            "AND hd.created < (SELECT MAX(created) FROM heparin_dosages) " +
            "ORDER BY hd.created DESC LIMIT 1",
        nativeQuery = true
    )
    fun getSecondsNewestByHeparinPatientId(heparinPatientId: Long): HeparinDosageEntity?
}
