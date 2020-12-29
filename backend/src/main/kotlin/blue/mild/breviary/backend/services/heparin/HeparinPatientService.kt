package blue.mild.breviary.backend.services.heparin

import blue.mild.breviary.backend.db.entities.HeparinPatientEntity
import blue.mild.breviary.backend.db.entities.PatientEntity
import blue.mild.breviary.backend.db.repositories.ApttValueRepository
import blue.mild.breviary.backend.db.repositories.HeparinDosageRepository
import blue.mild.breviary.backend.db.repositories.HeparinPatientRepository
import blue.mild.breviary.backend.db.repositories.PatientRepository
import blue.mild.breviary.backend.db.repositories.extensions.findByIdOrThrow
import blue.mild.breviary.backend.dtos.HeparinPatientDtoIn
import blue.mild.breviary.backend.dtos.HeparinPatientDtoOut
import blue.mild.breviary.backend.dtos.HeparinPatientWithDataDtoOut
import blue.mild.breviary.backend.dtos.PatientDtoIn
import blue.mild.breviary.backend.errors.EntityNotFoundBreviaryException
import blue.mild.breviary.backend.extensions.toDtoOut
import blue.mild.breviary.backend.services.AuthenticationService
import org.springframework.stereotype.Service
import java.time.temporal.ChronoUnit
import javax.transaction.Transactional

/**
 * HeparinPatientService.
 */
@Suppress("LongParameterList")
@Service
class HeparinPatientService(
    private val patientRepository: PatientRepository,
    private val heparinPatientRepository: HeparinPatientRepository,
    private val authenticationService: AuthenticationService,
    private val apttValueRepository: ApttValueRepository,
    private val heparinDosageRepository: HeparinDosageRepository
) {

    /**
     * Gets active heparin patients.
     *
     * @return [List<HeparinPatientDtoOut>]
     */
    fun getActiveHeparinPatients(): List<HeparinPatientDtoOut> =
        heparinPatientRepository.getActivePatients().map { it.toDtoOut() }

    /**
     * Creates heparin patient.
     *
     * @param heparinPatient [HeparinPatientDtoIn]
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
                dateOfBirth = heparinPatient.patient.dateOfBirth.truncatedTo(ChronoUnit.DAYS),
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
                solutionMilliliters = heparinPatient.solutionMilliliters,
                weight = heparinPatient.weight,
                createdBy = user
            )
        ).toDtoOut()
    }

    /**
     * Gets heparin patient.
     *
     * @param heparinPatientId [Long]
     * @return [HeparinPatientDtoOut]
     */
    @Throws(EntityNotFoundBreviaryException::class)
    fun getHeparinPatientById(heparinPatientId: Long): HeparinPatientDtoOut =
        heparinPatientRepository.findByIdOrThrow(heparinPatientId).toDtoOut()

    /**
     * Gets heparin patient with data.
     *
     * @param heparinPatientId [Long]
     * @return [HeparinPatientWithDataDtoOut]
     */
    @Throws(EntityNotFoundBreviaryException::class)
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
            actualDosage = actualHeparinDosage?.dosageContinuous,
            previousDosage = previousHeparinDosage?.dosageContinuous
        )
    }

    /**
     * Updates heparin patient.
     *
     * @param heparinPatientId [Long]
     * @param patient [PatientDtoIn]
     * @return [HeparinPatientDtoOut]
     */
    @Throws(EntityNotFoundBreviaryException::class)
    @Transactional
    fun updateHeparinPatient(heparinPatientId: Long, patient: PatientDtoIn): HeparinPatientDtoOut {
        val patientEntity = patientRepository.findByIdOrThrow(heparinPatientId)
        patientRepository.save(
            patientEntity.copy(
                firstName = patient.firstName,
                lastName = patient.lastName,
                dateOfBirth = patient.dateOfBirth.truncatedTo(ChronoUnit.DAYS),
                height = patient.height,
                sex = patient.sex,
                otherParams = patient.otherParams
            )
        )
        return heparinPatientRepository.findByIdOrThrow(heparinPatientId).toDtoOut()
    }
}
