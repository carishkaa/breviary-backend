package blue.mild.breviary.backend.db.entities

import java.util.Objects
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(
    name = "users",
    schema = "public",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["id"], name = "pk_users__id")
        // idx_users__username is created in migration
    ]
)
/**
 * UserEntity.
 */
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_generator")
    @SequenceGenerator(name = "users_generator", sequenceName = "users_seq", allocationSize = 50)
    val id: Long = 0,

    @Column(name = "username", nullable = false, length = 512)
    val username: String,

    @Column(name = "password", nullable = false, length = 512)
    val password: String,

    @Column(name = "active", nullable = false)
    val active: Boolean = true,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    val roles: MutableSet<UserRoleEntity> = mutableSetOf()

) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = Objects.hash(id)

    /**
     * Overriding default toString implementation as it leads to StackOverflow exception -
     * need to break the loop in role and groups.
     *
     * Note that the groups relation do not cause SO at the moment because UserGroup is not a data class.
     * I believe that providing the group name in group list is more valuable.
     */
    override fun toString(): String =
        "UserEntity(username='$username', password=*****)"
}
