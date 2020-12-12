package blue.mild.breviary.backend.db.repositories

import blue.mild.breviary.backend.db.entities.CarbohydrateIntakeValueEntity
import org.springframework.data.repository.CrudRepository

interface CarbohydrateIntakeValueRepository : CrudRepository<CarbohydrateIntakeValueEntity, Long>
