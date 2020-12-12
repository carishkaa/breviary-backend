package blue.mild.breviary.backend.services

import blue.mild.breviary.backend.db.entities.HeparinPatientEntity
import blue.mild.breviary.backend.db.entities.InsulinPatientEntity
import blue.mild.breviary.backend.db.entities.PatientEntity
import blue.mild.breviary.backend.db.repositories.HeparinPatientRepository
import blue.mild.breviary.backend.db.repositories.InsulinPatientRepository
import blue.mild.breviary.backend.db.repositories.PatientRepository
import blue.mild.breviary.backend.dtos.HeparinPatientDtoIn
import blue.mild.breviary.backend.dtos.HeparinPatientDtoOut
import blue.mild.breviary.backend.dtos.InsulinPatientDtoIn
import blue.mild.breviary.backend.dtos.InsulinPatientDtoOut
import blue.mild.breviary.backend.extensions.toDtoOut
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * PatientService.
 */
@Service
class PatientService(
    val patientRepository: PatientRepository,
    val heparinPatientRepository: HeparinPatientRepository,
    val insulinPatientRepository: InsulinPatientRepository,
    val authenticationService: AuthenticationService
) {

    /**
     * Creates heparin patient.
     *
     * @param heparinPatient
     * @return [HeparinPatientDtoOut]
     */
    @Transactional
    fun createHeparinPatient(heparinPatient: HeparinPatientDtoIn): HeparinPatientDtoOut {
        val user = authenticationService.getUser()
        val patientEntity = patientRepository.save(
            PatientEntity(
                id = 0,
                firstName = heparinPatient.patient.firstName,
                lastName = heparinPatient.patient.lastName,
                dateOfBirth = heparinPatient.patient.dateOfBirth,
                height = heparinPatient.patient.height,
                sex = heparinPatient.patient.sex,
                active = true,
                otherParams = heparinPatient.patient.otherParams,
                createdBy = user
            )
        )

        return heparinPatientRepository.save(
            HeparinPatientEntity(
                id = 0,
                patient = patientEntity,
                targetApptLow = heparinPatient.targetApptLow,
                targetApptHigh = heparinPatient.targetApptHigh,
                solutionHeparinIu = heparinPatient.solutionHeparinIu,
                solutionMl = heparinPatient.solutionMl,
                weight = heparinPatient.weight,
                createdBy = user
            )
        ).toDtoOut()
    }

    /**
     * Creates insulin patient.
     *
     * @param insulinPatient
     * @return [InsulinPatientDtoOut]
     */
    @Transactional
    fun createInsulinPatient(insulinPatient: InsulinPatientDtoIn): InsulinPatientDtoOut {
        val user = authenticationService.getUser()
        val patientEntity = patientRepository.save(
            PatientEntity(
                id = 0,
                firstName = insulinPatient.patient.firstName,
                lastName = insulinPatient.patient.lastName,
                dateOfBirth = insulinPatient.patient.dateOfBirth,
                height = insulinPatient.patient.height,
                sex = insulinPatient.patient.sex,
                active = true,
                otherParams = insulinPatient.patient.otherParams,
                createdBy = user
            )
        )

        return insulinPatientRepository.save(
            InsulinPatientEntity(
                id = 0,
                patient = patientEntity,
                tddi = insulinPatient.tddi,
                targetGlycemia = insulinPatient.targetGlycemia,
                createdBy = user
            )
        ).toDtoOut()
    }
}
