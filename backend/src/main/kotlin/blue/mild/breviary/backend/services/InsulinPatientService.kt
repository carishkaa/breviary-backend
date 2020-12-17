package blue.mild.breviary.backend.services

import blue.mild.breviary.backend.db.entities.InsulinPatientEntity
import blue.mild.breviary.backend.db.entities.PatientEntity
import blue.mild.breviary.backend.db.repositories.InsulinPatientRepository
import blue.mild.breviary.backend.db.repositories.PatientRepository
import blue.mild.breviary.backend.db.repositories.extensions.findByIdOrThrow
import blue.mild.breviary.backend.dtos.InsulinPatientDtoIn
import blue.mild.breviary.backend.dtos.InsulinPatientDtoOut
import blue.mild.breviary.backend.dtos.InsulinPatientWithDataDtoOut
import blue.mild.breviary.backend.dtos.PatientDtoIn
import blue.mild.breviary.backend.extensions.toDtoOut
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * InsulinPatientService.
 */
@Suppress("LongParameterList")
@Service
class InsulinPatientService(
    private val patientRepository: PatientRepository,
    private val insulinPatientRepository: InsulinPatientRepository,
    private val authenticationService: AuthenticationService
) {
    /**
     * Gets active insuline patients.
     *
     * @return [List<InsulinPatientDtoOut>]
     */
    fun getActiveInsulinPatients(): List<InsulinPatientDtoOut> =
        insulinPatientRepository.getActivePatients().map { it.toDtoOut() }

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
                createdBy = user,
                insulinType = insulinPatient.insulinType
            )
        ).toDtoOut()
    }

    /**
     * Gets inslin patient.
     *
     * @param insulinPatientId
     * @return
     */
    fun getInsulinPatientById(insulinPatientId: Long): InsulinPatientDtoOut =
        insulinPatientRepository.findByIdOrThrow(insulinPatientId).toDtoOut()

    /**
     * Gets insulin patient with data.
     *
     * @param insulinPatientId
     * @return
     */
    fun getInsulinPatientWithDataById(insulinPatientId: Long): InsulinPatientWithDataDtoOut {
        val insulinPatient = insulinPatientRepository.findByIdOrThrow(insulinPatientId).toDtoOut()

        return InsulinPatientWithDataDtoOut(
            insulinPatient = insulinPatient
        )
    }

    /**
     * Update insulin patient.
     *
     * @param insulinPatientId
     * @param patient
     * @return
     */
    @Transactional
    fun updateInsulinPatient(insulinPatientId: Long, patient: PatientDtoIn): InsulinPatientDtoOut {
        val patientEntity = patientRepository.findByIdOrThrow(insulinPatientId)
        patientRepository.save(
            patientEntity.copy(
                firstName = patient.firstName,
                lastName = patient.lastName,
                dateOfBirth = patient.dateOfBirth,
                height = patient.height,
                sex = patient.sex,
                otherParams = patient.otherParams
            )
        )
        return insulinPatientRepository.findByIdOrThrow(insulinPatientId).toDtoOut()
    }
}
