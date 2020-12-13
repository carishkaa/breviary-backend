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
    name = "aptt_values",
    schema = "public",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["id"], name = "pk_aptt_values__id")
    ]
)

/**
 * ApttValueEntity.
 */
data class ApttValueEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aptt_values_generator")
    @SequenceGenerator(name = "aptt_values_generator", sequenceName = "aptt_values_seq", allocationSize = 50)
    val id: Long = 0,

    @Column(name = "value", nullable = false)
    val value: Float,

    @ManyToOne
    @JoinColumn(
        name = "heparin_patient_id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_aptt_values__heparin_patients__patient_id")
    )
    val heparinPatient: HeparinPatientEntity,

    @ManyToOne
    @JoinColumn(
        name = "created_by",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_aptt_values__users__user_id")
    )
    val createdBy: UserEntity

) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ApttValueEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = Objects.hash(id)
}
