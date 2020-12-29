package blue.mild.breviary.backend.db.entities

import blue.mild.breviary.backend.enums.InsulinType
import org.hibernate.annotations.Type
import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
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
    name = "insulin_patients",
    schema = "public",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["patient_id"], name = "pk_insulin_patients__patient_id")
    ]
)
/**
 * InsulinPatientEntity.
 */
data class InsulinPatientEntity(
    @Id
    @Column(name = "patient_id", nullable = false)
    val id: Long = 0,

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    val patient: PatientEntity,

    @Column(name = "tddi", nullable = false)
    val tddi: Float,

    @Column(name = "target_glycemia", nullable = false)
    val targetGlycemia: Float,

    @Type(type = "pgsql_enum")
    @Enumerated(EnumType.STRING)
    @Column(name = "insulin_type", nullable = false)
    val insulinType: InsulinType,

    @ManyToOne
    @JoinColumn(
        name = "created_by",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_insulin_patients__users__user_id")
    )
    val createdBy: UserEntity
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
