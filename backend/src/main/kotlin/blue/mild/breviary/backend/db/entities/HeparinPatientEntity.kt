package blue.mild.breviary.backend.db.entities

import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.ForeignKey
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.MapsId
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(
    name = "heparin_patients",
    schema = "public",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["patient_id"], name = "pk_heparin_patients__patient_id")
    ]
)
/**
 * HeparinPatientEntity.
 */
data class HeparinPatientEntity(
    @Id
    @Column(name = "patient_id", nullable = false)
    val id: Long = 0,

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    val patient: PatientEntity,

    @Column(name = "target_aptt_low", nullable = false)
    val targetApttLow: Float,

    @Column(name = "target_aptt_high", nullable = false)
    val targetApttHigh: Float,

    @Column(name = "solution_heparin_units", nullable = false)
    val solutionHeparinUnits: Float,

    @Column(name = "solution_milliliters", nullable = false)
    val solutionMilliliters: Float,

    @Column(name = "weight", nullable = false)
    val weight: Float,

    @ManyToOne
    @JoinColumn(
        name = "created_by",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_heparin_patients__users__user_id")
    )
    val createdBy: UserEntity

) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HeparinPatientEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = Objects.hash(id)
}
