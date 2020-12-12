package blue.mild.breviary.backend.extensions

import blue.mild.breviary.backend.db.entities.InsulinPatientEntity
import blue.mild.breviary.backend.dtos.InsulinPatientDtoOut
import blue.mild.breviary.backend.utils.encodeID

/**
 * [InsulinPatientEntity] to [InsulinPatientDtoOut].
 *
 * @return [InsulinPatientDtoOut]
 */
fun InsulinPatientEntity.toDtoOut(): InsulinPatientDtoOut = InsulinPatientDtoOut(
    id = this.id.encodeID("insulin-patient"),
    patient = this.patient.toDtoOut(),
    targetGlycemia = this.targetGlycemia,
    tddi = this.tddi
)
