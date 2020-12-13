package blue.mild.breviary.backend.extensions

import blue.mild.breviary.backend.db.entities.HeparinPatientEntity
import blue.mild.breviary.backend.dtos.HeparinPatientDtoOut
import blue.mild.breviary.backend.utils.encodeID

/**
 * [HeparinPatientEntity] to [HeparinPatientDtoOut].
 *
 * @return [HeparinPatientDtoOut]
 */
fun HeparinPatientEntity.toDtoOut(): HeparinPatientDtoOut = HeparinPatientDtoOut(
    id = this.id.encodeID("heparin-patient"),
    patient = this.patient.toDtoOut(),
    targetApttLow = this.targetApttLow,
    targetApttHigh = this.targetApttHigh,
    solutionHeparinUnits = this.solutionHeparinUnits,
    solutionMl = this.solutionMl,
    weight = this.weight
)
