package blue.mild.breviary.backend.services

import blue.mild.breviary.backend.db.repositories.ApttValueRepository
import blue.mild.breviary.backend.db.repositories.CarbohydrateIntakeValueRepository
import blue.mild.breviary.backend.db.repositories.GlycemiaValueRepository
import blue.mild.breviary.backend.db.repositories.HeparinDosageRepository
import blue.mild.breviary.backend.db.repositories.HeparinPatientRepository
import blue.mild.breviary.backend.db.repositories.InsulinDosageRepository
import blue.mild.breviary.backend.db.repositories.InsulinPatientRepository
import blue.mild.breviary.backend.db.repositories.extensions.findByIdOrThrow
import blue.mild.breviary.backend.dtos.HeparinPatientHistoryEntryDtoOut
import blue.mild.breviary.backend.dtos.InsulinPatientHistoryEntryDtoOut
import org.springframework.stereotype.Service
import kotlin.math.min

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
        heparinPatientRepository.findByIdOrThrow(heparinPatientId)
        val heparinDosages = heparinDosageRepository.getByHeparinPatientId(heparinPatientId)
        val apttValues = apttValueRepository.getByHeparinPatientId(heparinPatientId)

        val minLen = min(heparinDosages.size, apttValues.size)
        return (0..minLen).map { i ->
            val heparinDosage = heparinDosages.elementAt(i)
            val apttValue = apttValues.elementAt(i)
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
        insulinPatientRepository.findByIdOrThrow(insulinPatientId)
        val insulinDosages = insulinDosageRepository.getByInsulinPatientId(insulinPatientId)
        val carbohydrateIntakeValues = carbohydrateIntakeValueRepository.getByInsulinPatientId(insulinPatientId)
        val glycemiaValues = glycemiaValueRepository.getByInsulinPatientId(insulinPatientId)

        val minLen = min(min(insulinDosages.size, carbohydrateIntakeValues.size), glycemiaValues.size)
        return (0..minLen).map { i ->
            val insulinDosage = insulinDosages.elementAt(i)
            val carbohydrateIntakeValue = carbohydrateIntakeValues.elementAt(i)
            val glycemiaValue = glycemiaValues.elementAt(i)
            InsulinPatientHistoryEntryDtoOut(
                date = insulinDosage.created,
                dosage = insulinDosage.dosage,
                carbohydrateIntake = carbohydrateIntakeValue.value,
                glycemiaValue = glycemiaValue.value
            )
        }
    }
}
