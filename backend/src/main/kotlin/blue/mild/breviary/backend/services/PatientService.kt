package blue.mild.breviary.backend.services

import blue.mild.breviary.backend.db.entities.HeparinPatientEntity
import blue.mild.breviary.backend.db.entities.InsulinPatientEntity
import blue.mild.breviary.backend.db.entities.PatientEntity
import blue.mild.breviary.backend.db.repositories.ApttValueRepository
import blue.mild.breviary.backend.db.repositories.HeparinDosageRepository
import blue.mild.breviary.backend.db.repositories.HeparinPatientRepository
import blue.mild.breviary.backend.db.repositories.InsulinPatientRepository
import blue.mild.breviary.backend.db.repositories.PatientRepository
import blue.mild.breviary.backend.db.repositories.extensions.findByIdOrThrow
import blue.mild.breviary.backend.dtos.HeparinPatientDtoIn
import blue.mild.breviary.backend.dtos.HeparinPatientDtoOut
import blue.mild.breviary.backend.dtos.HeparinPatientWithDataDtoOut
import blue.mild.breviary.backend.dtos.InsulinPatientDtoIn
import blue.mild.breviary.backend.dtos.InsulinPatientDtoOut
import blue.mild.breviary.backend.dtos.InsulinPatientWithDataDtoOut
import blue.mild.breviary.backend.dtos.PatientDtoIn
import blue.mild.breviary.backend.extensions.toDtoOut
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * PatientService.
 */
@Suppress("LongParameterList")
@Service
class PatientService(
    val patientRepository: PatientRepository,
    val heparinPatientRepository: HeparinPatientRepository,
    val insulinPatientRepository: InsulinPatientRepository,
    val authenticationService: AuthenticationService,
    val apttValueRepository: ApttValueRepository,
    val heparinDosageRepository: HeparinDosageRepository
) {

    /**
     * Gets active heparin patients.
     *
     * @return [List<HeparinPatientDtoOut>]
     */
    fun getActiveHeparinPatients(): List<HeparinPatientDtoOut> =
        heparinPatientRepository.getActivePatients().map { it.toDtoOut() }

    /**
     * Gets active insuline patients.
     *
     * @return [List<InsulinPatientDtoOut>]
     */
    fun getActiveInsulinPatients(): List<InsulinPatientDtoOut> =
        insulinPatientRepository.getActivePatients().map { it.toDtoOut() }

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
                targetApttLow = heparinPatient.targetApttLow,
                targetApttHigh = heparinPatient.targetApttHigh,
                solutionHeparinUnits = heparinPatient.solutionHeparinUnits,
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

    /**
     * Gets heparin patient.
     *
     * @param heparinPatientId
     * @return
     */
    fun getHeparinPatientById(heparinPatientId: Long): HeparinPatientDtoOut =
        heparinPatientRepository.findByIdOrThrow(heparinPatientId).toDtoOut()

    /**
     * Gets inslin patient.
     *
     * @param insulinPatientId
     * @return
     */
    fun getInsulinPatientById(insulinPatientId: Long): InsulinPatientDtoOut =
        insulinPatientRepository.findByIdOrThrow(insulinPatientId).toDtoOut()

    /**
     * Gets heparin patient with data.
     *
     * @param heparinPatientId
     * @return
     */
    fun getHeparinPatientWithDataById(heparinPatientId: Long): HeparinPatientWithDataDtoOut {
        val heparinPatient = heparinPatientRepository.findByIdOrThrow(heparinPatientId).toDtoOut()

        val actualAptt = apttValueRepository.getNewestByHeparinPatientId(heparinPatientId)
        val previousAptt = apttValueRepository.getSecondsNewestByHeparinPatientId(heparinPatientId)

        val actualHeparinDosage = heparinDosageRepository.getNewestByHeparinPatientId(heparinPatientId)
        val previousHeparinDosage = heparinDosageRepository.getSecondsNewestByHeparinPatientId(heparinPatientId)

        return HeparinPatientWithDataDtoOut(
            heparinPatient = heparinPatient,
            actualAptt = actualAptt?.value,
            actualApttUpdatedOn = actualAptt?.created,
            previousAptt = previousAptt?.value,
            actualDosage = actualHeparinDosage?.dosageHeparinContinuous,
            previousDosage = previousHeparinDosage?.dosageHeparinContinuous
        )
    }

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
     * Updates heparin patient.
     *
     * @param heparinPatientId
     * @param patient
     * @return
     */
    @Transactional
    fun updateHeparinPatient(heparinPatientId: Long, patient: PatientDtoIn): HeparinPatientDtoOut {
        val patientEntity = patientRepository.findByIdOrThrow(heparinPatientId)
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
        return heparinPatientRepository.findByIdOrThrow(heparinPatientId).toDtoOut()
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
