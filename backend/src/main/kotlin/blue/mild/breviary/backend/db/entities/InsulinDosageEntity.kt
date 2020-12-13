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
    name = "insulin_dosages",
    schema = "public",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["id"], name = "pk_insulin_dosages__id")
    ]
)

/**
 * InsulinDosageEntity.
 */
data class InsulinDosageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "insulin_dosages_generator")
    @SequenceGenerator(name = "insulin_dosages_generator", sequenceName = "insulin_dosages_seq", allocationSize = 50)
    val id: Long = 0,

    @Column(name = "dosage_insulin", nullable = false)
    val dosageInsulin: Float,

    @ManyToOne
    @JoinColumn(
        name = "heparin_patient_id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_insulin_dosages__insulin_patients__patient_id")
    )
    val insulinPatient: InsulinPatientEntity,

    @ManyToOne
    @JoinColumn(
        name = "created_by",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_insulin_dosages__users__user_id")
    )
    val createdBy: UserEntity

) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InsulinDosageEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = Objects.hash(id)
}
