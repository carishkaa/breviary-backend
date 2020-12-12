package blue.mild.breviary.backend.db.entities

import blue.mild.breviary.backend.enums.Sex
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.time.Instant
import java.util.HashMap
import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
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
    name = "patients",
    schema = "public",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["id"], name = "pk_patients__id")
    ]
)

@TypeDefs(
    TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType::class
    ),
    TypeDef(
        name = "jsonb",
        typeClass = JsonBinaryType::class
    )
)

/**
 * PatientEntity.
 */
data class PatientEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patients_generator")
    @SequenceGenerator(name = "patients_generator", sequenceName = "patients_seq", allocationSize = 50)
    val id: Long = 0,

    @Column(name = "first_name", nullable = false, length = 512)
    val firstName: String,

    @Column(name = "last_name", nullable = false, length = 512)
    val lastName: String,

    @Column(name = "date_of_birth", nullable = false)
    val dateOfBirth: Instant,

    @Column(name = "height", nullable = false)
    val height: Int,

    @Type(type = "pgsql_enum")
    @Enumerated(EnumType.STRING)
    @Column(name = "sex", nullable = false)
    val sex: Sex,

    @Column(name = "active", nullable = false)
    val active: Boolean = false,

    @Type(type = "jsonb")
    @Column(name = "other_params", nullable = false)
    val otherParams: HashMap<String, String>,

    @ManyToOne
    @JoinColumn(
        name = "created_by",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_patients__users__user_id")
    )
    val createdBy: UserEntity

) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PatientEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = Objects.hash(id)
}
