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
    name = "heparin_dosages",
    schema = "public",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["id"], name = "pk_heparin_dosages__id")
    ]
)

/**
 * HeparinDosageEntity.
 */
data class HeparinDosageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "heparin_dosages_generator")
    @SequenceGenerator(name = "heparin_dosages_generator", sequenceName = "heparin_dosages_seq", allocationSize = 50)
    val id: Long = 0,

    @Column(name = "dosage_heparin_continuous", nullable = false)
    val dosageHeparinContinuous: Int,

    @Column(name = "dosage_heparin_bolus", nullable = false)
    val dosageHeparinBolus: Int,

    @ManyToOne
    @JoinColumn(
        name = "heparin_patient_id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_heparin_dosages__heparin_patients__patient_id")
    )
    val heparinPatient: HeparinPatientEntity

) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HeparinDosageEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = Objects.hash(id)
}
