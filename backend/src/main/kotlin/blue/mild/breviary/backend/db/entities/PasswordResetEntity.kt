package blue.mild.breviary.backend.db.entities

import java.time.Instant
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
    name = "password_resets",
    schema = "public",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["id"], name = "pk_password_resets__id"),
        UniqueConstraint(columnNames = ["token"], name = "uk_password_resets__token")
    ]
)
/**
 * PasswordResetEntity.
 */
data class PasswordResetEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "password_resets_generator")
    @SequenceGenerator(
        name = "password_resets_generator",
        sequenceName = "password_resets_seq",
        allocationSize = 50
    )
    val id: Long = 0,

    @Column(name = "token", nullable = false, length = 512)
    val token: String,

    @Column(name = "valid_until", updatable = true, nullable = false)
    val validUntil: Instant,

    @ManyToOne
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_password_resets__user_id")
    )
    val user: UserEntity

) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PasswordResetEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = Objects.hash(id)
}
