package blue.mild.breviary.backend.db.entities

import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ForeignKey
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(
    name = "heparin_patients",
    schema = "public",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["id"], name = "pk_heparin_patients__id")
    ]
)
/**
 * HeparinPatientEntity.
 */
data class HeparinPatientEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "heparin_patients_generator")
    @SequenceGenerator(name = "heparin_patients_generator", sequenceName = "heparin_patients_seq", allocationSize = 50)
    val id: Long = 0,

    @Column(name = "target_aptt_low", nullable = false)
    val targetApptLow: Int,

    @Column(name = "target_aptt_high", nullable = false)
    val targetApptHigh: Int,

    @Column(name = "solution_heparin_iu", nullable = false)
    val solutionHeparinIu: Int,

    @Column(name = "solution_ml", nullable = false)
    val solutionMl: Int,

    @Column(name = "weight", nullable = false)
    val weight: Int,

    @ManyToOne
    @JoinColumn(
        name = "created_by",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_heparin_patients__users__user_id")
    )
    val createdBy: UserEntity,

    @ManyToOne
    @JoinColumn(
        name = "patient_id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_heparin_patients__patients__patient_id")
    )
    val patient: PatientEntity

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
