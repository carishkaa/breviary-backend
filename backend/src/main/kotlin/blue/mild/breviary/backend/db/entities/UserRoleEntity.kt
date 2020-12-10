package blue.mild.breviary.backend.db.entities

import java.util.Objects
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
    name = "user_roles",
    schema = "public",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["id"], name = "pk_user_roles__id")
    ]
)
/**
 * UserRoleEntity.
 */
data class UserRoleEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_roles_generator")
    @SequenceGenerator(
        name = "user_roles_generator",
        sequenceName = "user_roles_seq",
        allocationSize = 50
    )
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_user_roles__user_id")
    )
    val user: UserEntity,

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false, foreignKey = ForeignKey(name = "fk_user_roles__role_id"))
    val role: RoleEntity

) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserRoleEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = Objects.hash(id)
}
