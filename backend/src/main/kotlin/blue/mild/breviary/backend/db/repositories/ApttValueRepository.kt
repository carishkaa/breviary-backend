package blue.mild.breviary.backend.db.repositories

import blue.mild.breviary.backend.db.entities.ApttValueEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface ApttValueRepository : CrudRepository<ApttValueEntity, Long> {
    fun getByHeparinPatientId(heparinPatientId: Long): Collection<ApttValueEntity>

    @Query(
        value = "SELECT av.* " +
            "FROM aptt_values av " +
            "WHERE av.heparin_patient_id = :heparinPatientId " +
            "ORDER BY av.created DESC LIMIT 1",
        nativeQuery = true
    )
    fun getNewestByHeparinPatientId(heparinPatientId: Long): ApttValueEntity?

    @Query(
        value = "SELECT av.* " +
            "FROM aptt_values av " +
            "WHERE av.heparin_patient_id = :heparinPatientId " +
            "AND av.created < (SELECT MAX(created) FROM aptt_values) " +
            "ORDER BY av.created DESC LIMIT 1",
        nativeQuery = true
    )
    fun getSecondsNewestByHeparinPatientId(heparinPatientId: Long): ApttValueEntity?
}
