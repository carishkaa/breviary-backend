package blue.mild.breviary.backend.db.entities

import blue.mild.breviary.backend.enums.UserRole
import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(
    name = "roles",
    schema = "public",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["name"], name = "uk_roles__name"),
        UniqueConstraint(columnNames = ["id"], name = "pk_roles__id")
    ]
)
/**
 * RoleEntity.
 */
data class RoleEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_generator")
    @SequenceGenerator(name = "roles_generator", sequenceName = "roles_seq", allocationSize = 50)
    val id: Long = 0,

    @Column(name = "name", nullable = false, length = 512)
    @Enumerated(EnumType.STRING)
    val name: UserRole

) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RoleEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = Objects.hash(id)
}
