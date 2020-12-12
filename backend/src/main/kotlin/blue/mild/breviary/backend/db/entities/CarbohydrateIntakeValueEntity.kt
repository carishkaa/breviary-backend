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
    name = "carbohydrate_intake_values",
    schema = "public",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["id"], name = "pk_carbohydrate_intake_values__id")
    ]
)

/**
 * CarbohydrateIntakeValueEntity.
 */
data class CarbohydrateIntakeValueEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "carbohydrate_intake_values_generator")
    @SequenceGenerator(
        name = "carbohydrate_intake_values_generator",
        sequenceName = "carbohydrate_intake_values_seq",
        allocationSize = 50
    )
    val id: Long = 0,

    @Column(name = "value", nullable = false)
    val value: Int,

    @ManyToOne
    @JoinColumn(
        name = "heparin_patient_id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_carbohydrate_intake_values__insulin_patients__patient_id")
    )
    val insulinPatient: InsulinPatientEntity

) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CarbohydrateIntakeValueEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = Objects.hash(id)
}
