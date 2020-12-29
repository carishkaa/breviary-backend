package blue.mild.breviary.backend.extensions

import blue.mild.breviary.backend.db.entities.PatientEntity
import blue.mild.breviary.backend.dtos.PatientDtoOut
import blue.mild.breviary.backend.utils.encodeID

/**
 * [PatientEntity] to [PatientDtoOut].
 *
 * @return [PatientDtoOut]
 */
fun PatientEntity.toDtoOut(): PatientDtoOut = PatientDtoOut(
    id = this.id.encodeID("patient"),
    firstName = this.firstName,
    lastName = this.lastName,
    dateOfBirth = this.dateOfBirth,
    height = this.height,
    sex = this.sex,
    otherParams = this.otherParams
)
