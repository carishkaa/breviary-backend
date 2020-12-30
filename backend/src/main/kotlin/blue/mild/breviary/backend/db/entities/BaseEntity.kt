package blue.mild.breviary.backend.db.entities

import blue.mild.breviary.backend.services.InstantTimeProvider
import java.time.Instant
import javax.persistence.Column
import javax.persistence.MappedSuperclass

@MappedSuperclass
/**
 * Base entity class with definition of table metadata for better data flow overview.
 * It should be inherited by every entity.
 */
abstract class BaseEntity {
    @Column(name = "created", insertable = true, updatable = false, nullable = false)
    val created: Instant = InstantTimeProvider.instance.now()

    @Column(name = "updated", insertable = false, updatable = true, nullable = false)
    val updated: Instant = InstantTimeProvider.instance.now()

    @Column(name = "deleted", nullable = true)
    val deleted: Instant? = null
}
