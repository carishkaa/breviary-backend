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
    name = "glycemia_values",
    schema = "public",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["id"], name = "pk_glycemia_values__id")
    ]
)

/**
 * GlycemiaValueEntity.
 */
data class GlycemiaValueEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "glycemia_values_generator")
    @SequenceGenerator(name = "glycemia_values_generator", sequenceName = "glycemia_values_seq", allocationSize = 50)
    val id: Long = 0,

    @Column(name = "value", nullable = false)
    val value: Float,

    @ManyToOne
    @JoinColumn(
        name = "heparin_patient_id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_glycemia_values__insulin_patients__patient_id")
    )
    val insulinPatient: InsulinPatientEntity,

    @ManyToOne
    @JoinColumn(
        name = "created_by",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_glycemia_values__users__user_id")
    )
    val createdBy: UserEntity

) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GlycemiaValueEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = Objects.hash(id)
}
