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
    name = "insulin_patients",
    schema = "public",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["id"], name = "pk_insulin_patients__id")
    ]
)
/**
 * InsulinPatientEntity.
 */
data class InsulinPatientEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "insulin_patients_generator")
    @SequenceGenerator(name = "insulin_patients_generator", sequenceName = "insulin_patients_seq", allocationSize = 50)
    val id: Long = 0,

    @Column(name = "tddi", nullable = false)
    val tddi: Int,

    @Column(name = "target_glycemia", nullable = false)
    val targetGlycemia: Int,

    @ManyToOne
    @JoinColumn(
        name = "created_by",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_insulin_patients__users__user_id")
    )
    val createdBy: UserEntity,

    @ManyToOne
    @JoinColumn(
        name = "patient_id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_insulin_patients__patients__patient_id")
    )
    val patient: PatientEntity

) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InsulinPatientEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = Objects.hash(id)
}
