package blue.mild.breviary.backend.services

import blue.mild.breviary.backend.db.repositories.ApttValueRepository
import blue.mild.breviary.backend.db.repositories.CarbohydrateIntakeValueRepository
import blue.mild.breviary.backend.db.repositories.GlycemiaValueRepository
import blue.mild.breviary.backend.db.repositories.HeparinDosageRepository
import blue.mild.breviary.backend.db.repositories.HeparinPatientRepository
import blue.mild.breviary.backend.db.repositories.InsulinDosageRepository
import blue.mild.breviary.backend.db.repositories.InsulinPatientRepository
import blue.mild.breviary.backend.db.repositories.extensions.assertEntityExists
import blue.mild.breviary.backend.dtos.HeparinPatientHistoryEntryDtoOut
import blue.mild.breviary.backend.dtos.InsulinPatientHistoryEntryDtoOut
import blue.mild.breviary.backend.extensions.zip
import org.springframework.stereotype.Service

/**
 * PatientHistoryEntriesService.
 */
@Suppress("LongParameterList")
@Service
class PatientHistoryEntriesService(
    private val heparinPatientRepository: HeparinPatientRepository,
    private val insulinPatientRepository: InsulinPatientRepository,
    private val heparinDosageRepository: HeparinDosageRepository,
    private val apttValueRepository: ApttValueRepository,
    private val insulinDosageRepository: InsulinDosageRepository,
    private val carbohydrateIntakeValueRepository: CarbohydrateIntakeValueRepository,
    private val glycemiaValueRepository: GlycemiaValueRepository
) {

    /**
     * Gets heparin patient history entries.
     *
     * @param heparinPatientId
     * @return
     */
    fun getHeparinPatientHistoryEntries(heparinPatientId: Long): List<HeparinPatientHistoryEntryDtoOut> {
        heparinPatientRepository.assertEntityExists(heparinPatientId)

        val heparinDosages = heparinDosageRepository.getByHeparinPatientId(heparinPatientId)
        val apttValues = apttValueRepository.getByHeparinPatientId(heparinPatientId)

        return heparinDosages.zip(apttValues) { heparinDosage, apttValue ->
            HeparinPatientHistoryEntryDtoOut(
                date = heparinDosage.created,
                aptt = apttValue.value,
                bolus = heparinDosage.dosageBolus,
                heparinContinuous = heparinDosage.dosageContinuous
            )
        }
    }

    /**
     * Gets insulin patient history entries.
     *
     * @param insulinPatientId
     * @return
     */
    fun getInsulinPatientHistoryEntries(insulinPatientId: Long): List<InsulinPatientHistoryEntryDtoOut> {
        insulinPatientRepository.assertEntityExists(insulinPatientId)

        val insulinDosages = insulinDosageRepository.getByInsulinPatientId(insulinPatientId)
        val carbohydrateIntakeValues = carbohydrateIntakeValueRepository.getByInsulinPatientId(insulinPatientId)
        val glycemiaValues = glycemiaValueRepository.getByInsulinPatientId(insulinPatientId)

        return insulinDosages.zip(carbohydrateIntakeValues, glycemiaValues) { insulinDosage, carbohydrateIntakeValue, glycemiaValue ->
            InsulinPatientHistoryEntryDtoOut(
                date = insulinDosage.created,
                dosage = insulinDosage.dosage,
                carbohydrateIntake = carbohydrateIntakeValue.value,
                glycemiaValue = glycemiaValue.value
            )
        }
    }
}
