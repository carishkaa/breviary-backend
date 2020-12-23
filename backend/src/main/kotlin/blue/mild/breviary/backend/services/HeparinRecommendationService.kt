package blue.mild.breviary.backend.services

import blue.mild.breviary.backend.db.entities.ApttValueEntity
import blue.mild.breviary.backend.db.entities.HeparinDosageEntity
import blue.mild.breviary.backend.db.repositories.ApttValueRepository
import blue.mild.breviary.backend.db.repositories.HeparinDosageRepository
import blue.mild.breviary.backend.db.repositories.HeparinPatientRepository
import blue.mild.breviary.backend.db.repositories.extensions.findByIdOrThrow
import blue.mild.breviary.backend.dtos.HeparinRecommendationDto
import blue.mild.breviary.backend.dtos.HeparinRecommendationDtoOut
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.transaction.Transactional
import kotlin.math.roundToInt

const val LOWEST_APTT = 1.2f
const val LOW_APTT = 1.5f
const val STANDARD_APTT = 2.3f
const val HIGH_APTT = 3f
const val HIGHEST_APTT = 3.5f

const val LOWEST_APTT_DOSAGE_PER_KG_CHANGE = 4f
const val LOW_APTT_DOSAGE_PER_KG_CHANGE = 2f
const val BELOW_STANDARD_APTT_DOSAGE_PER_KG_CHANGE = 1f
const val ABOVE_STANDARD_APTT_DOSAGE_PER_KG_CHANGE = -1f
const val HIGH_APTT_DOSAGE_PER_KG_CHANGE = -2f
const val HIGHEST_APTT_DOSAGE_PER_KG_CHANGE = -3f

const val LOWEST_APTT_BOLUS = 80f
const val LOW_APTT_BOLUS = 40f

const val REMAINDER_STANDARD_HOURS = 6
const val REMAINDER_FIRST_HOURS = 4
const val REMINDER_NON_COAGULATING_HOURS = 1

const val EXTREME_DOSAGE_DIFF = 15f

const val MIN_WEIGHT_KG = 50f
const val MAX_WEIGHT_KG = 100f

const val DEFAULT_UNITS_PER_KG = 18f

/**
 * Helper data structure for heparin recommendation.
 *
 * @property heparinContinuousDosage
 * @property heparinBolusDosage
 */
data class RecommendedHeparinDosage(val heparinContinuousDosage: Float, val heparinBolusDosage: Float)

/**
 * HeparinRecommendationService.
 */
@Service
class HeparinRecommendationService(
    private val heparinPatientRepository: HeparinPatientRepository,
    private val authenticationService: AuthenticationService,
    private val apttValueRepository: ApttValueRepository,
    private val heparinDosageRepository: HeparinDosageRepository,
    private val instantTimeProvider: InstantTimeProvider
) {

    /**
     * Creates heparin recommendation.
     *
     * @param heparinPatientId [Long]
     * @param currentAptt [Float]
     * @return [HeparinRecommendationDtoOut]
     */
    @Transactional
    fun createHeparinRecommendation(heparinPatientId: Long, currentAptt: Float): HeparinRecommendationDtoOut {
        val heparinPatientEntity = heparinPatientRepository.findByIdOrThrow(heparinPatientId)
        val previousAptt = apttValueRepository.getNewestByHeparinPatientId(heparinPatientId)

        apttValueRepository.save(
            ApttValueEntity(
                value = currentAptt,
                heparinPatient = heparinPatientEntity,
                createdBy = authenticationService.getUser()
            )
        )

        val currentDosage = heparinDosageRepository.getNewestByHeparinPatientId(heparinPatientId)
        val previousDosage = heparinDosageRepository.getSecondsNewestByHeparinPatientId(heparinPatientId)

        val calculatedHeparinRecommendation = calculateHeparinRecommendation(
            weight = heparinPatientEntity.weight,
            targetApttLow = heparinPatientEntity.targetApttLow,
            targetApttHigh = heparinPatientEntity.targetApttHigh,
            currentAptt = currentAptt,
            previousAptt = previousAptt?.value,
            solutionHeparinUnits = heparinPatientEntity.solutionHeparinUnits,
            solutionMilliliters = heparinPatientEntity.solutionMilliliters,
            currentContinuousDosage = currentDosage?.dosageContinuous,
            previousContinuousDosage = previousDosage?.dosageContinuous
        )

        heparinDosageRepository.save(
            HeparinDosageEntity(
                dosageContinuous = calculatedHeparinRecommendation.dosageContinuous,
                dosageBolus = calculatedHeparinRecommendation.dosageBolus,
                heparinPatient = heparinPatientEntity,
                createdBy = authenticationService.getUser()
            )
        )

        return HeparinRecommendationDtoOut(
            actualHeparinContinuousDosage = calculatedHeparinRecommendation.dosageContinuous,
            previousHeparinContinuousDosage = currentDosage?.dosageContinuous,
            actualHeparinBolusDosage = calculatedHeparinRecommendation.dosageBolus,
            previousHeparinBolusDosage = currentDosage?.dosageBolus,
            nextRemainder = calculatedHeparinRecommendation.nextRemainder,
            doctorWarning = calculatedHeparinRecommendation.doctorWarning
        )
    }

    @Suppress("LongParameterList", "LongMethod", "ComplexMethod", "ReturnCount")
    private fun calculateRecommendedDosage(
        weight: Float,
        targetApttLow: Float,
        targetApttHigh: Float,
        currentAptt: Float?,
        solutionHeparinUnits: Float,
        solutionMilliliters: Float,
        currentContinuousDosage: Float?,
        previousContinuousDosage: Float?,
    ): RecommendedHeparinDosage {
        if (currentAptt == null || currentContinuousDosage == null) {
            // initial setup, no measurements yet
            return RecommendedHeparinDosage(
                defaultHeparinContinuousDosage(weight, solutionHeparinUnits, solutionMilliliters),
                0f
            )
        }

        if (currentContinuousDosage == 0f) {
            return RecommendedHeparinDosage(
                getNewDosage(
                    previousContinuousDosage!!,
                    weight,
                    HIGHEST_APTT_DOSAGE_PER_KG_CHANGE,
                    solutionHeparinUnits,
                    solutionMilliliters
                ),
                0f
            )
        }

        if (currentAptt < LOWEST_APTT) {
            return RecommendedHeparinDosage(
                getNewDosage(
                    currentContinuousDosage,
                    weight,
                    LOWEST_APTT_DOSAGE_PER_KG_CHANGE,
                    solutionHeparinUnits,
                    solutionMilliliters
                ),
                calculateBolus(weight, solutionHeparinUnits, solutionMilliliters, LOWEST_APTT_BOLUS)
            )
        }

        if (currentAptt < LOW_APTT) {
            return RecommendedHeparinDosage(
                getNewDosage(
                    currentContinuousDosage,
                    weight,
                    LOW_APTT_DOSAGE_PER_KG_CHANGE,
                    solutionHeparinUnits,
                    solutionMilliliters
                ),
                calculateBolus(weight, solutionHeparinUnits, solutionMilliliters, LOW_APTT_BOLUS)
            )
        }

        if (currentAptt < targetApttLow) {
            return RecommendedHeparinDosage(
                getNewDosage(
                    currentContinuousDosage,
                    weight,
                    BELOW_STANDARD_APTT_DOSAGE_PER_KG_CHANGE,
                    solutionHeparinUnits,
                    solutionMilliliters
                ),
                0f
            )
        }

        if (currentAptt < targetApttHigh) {
            return RecommendedHeparinDosage(currentContinuousDosage, 0f)
        }

        if (currentAptt < STANDARD_APTT) {
            return RecommendedHeparinDosage(
                getNewDosage(
                    currentContinuousDosage,
                    weight,
                    ABOVE_STANDARD_APTT_DOSAGE_PER_KG_CHANGE,
                    solutionHeparinUnits,
                    solutionMilliliters
                ),
                0f
            )
        }

        if (currentAptt <= HIGH_APTT) {
            return RecommendedHeparinDosage(
                getNewDosage(
                    currentContinuousDosage,
                    weight,
                    HIGH_APTT_DOSAGE_PER_KG_CHANGE,
                    solutionHeparinUnits,
                    solutionMilliliters
                ),
                0f
            )
        }

        return RecommendedHeparinDosage(0f, 0f)
    }

    private fun calculateBolus(
        weight: Float,
        solutionHeparinUnits: Float,
        solutionMilliliters: Float,
        unitsPerKg: Float
    ): Float =
        unitsPerKg * weight * solutionMilliliters / solutionHeparinUnits

    @Suppress("UnnecessaryParentheses")
    private fun getNewDosage(
        currentDosage: Float,
        weight: Float,
        unitsPerKg: Float,
        solutionHeparinUnits: Float,
        solutionMilliliters: Float
    ): Float = currentDosage + (unitsPerKg * weight * solutionMilliliters / solutionHeparinUnits)

    @Suppress("LongParameterList")
    private fun calculateHeparinRecommendation(
        weight: Float,
        targetApttLow: Float,
        targetApttHigh: Float,
        currentAptt: Float?,
        previousAptt: Float?,
        solutionHeparinUnits: Float,
        solutionMilliliters: Float,
        currentContinuousDosage: Float?,
        previousContinuousDosage: Float?
    ): HeparinRecommendationDto {

        val recommendedHeparinDosage = calculateRecommendedDosage(
            weight,
            targetApttLow,
            targetApttHigh,
            currentAptt,
            solutionHeparinUnits,
            solutionMilliliters,
            currentContinuousDosage,
            previousContinuousDosage
        )

        val nextRemainder = getNextRemainder(currentAptt, recommendedHeparinDosage.heparinContinuousDosage)
        val doctorWarning = getDoctorWarning(
            currentAptt,
            previousAptt,
            recommendedHeparinDosage.heparinContinuousDosage,
            weight,
            solutionHeparinUnits,
            solutionMilliliters
        )

        return HeparinRecommendationDto(
            dosageContinuous = recommendedHeparinDosage.heparinContinuousDosage,
            dosageBolus = recommendedHeparinDosage.heparinBolusDosage,
            nextRemainder = nextRemainder,
            doctorWarning = doctorWarning
        )
    }

    @Suppress("ReturnCount")
    private fun getDoctorWarning(
        currentAptt: Float?,
        previousAptt: Float?,
        heparinContinuousDosage: Float,
        weight: Float,
        solutionHeparinUnits: Float,
        solutionMilliliters: Float
    ): String {
        val dosageDiff = kotlin.math.abs(
            heparinContinuousDosage - defaultHeparinContinuousDosage(
                weight,
                solutionHeparinUnits,
                solutionMilliliters
            )
        )

        if (currentAptt == null || previousAptt == null) {
            return ""
        }

        if (currentAptt < LOWEST_APTT && previousAptt < LOWEST_APTT) {
            return "APTT below $LOWEST_APTT for 2 consecutive measurements."
        }

        if (currentAptt > HIGHEST_APTT && previousAptt > HIGHEST_APTT) {
            return "APTT above $HIGHEST_APTT for 2 consecutive measurements."
        }

        if (dosageDiff >= EXTREME_DOSAGE_DIFF && heparinContinuousDosage > 0) {
            return "Current continuous heparin dosage differs from default weight based dosage by ${
                dosageDiff.roundToInt()
            }."
        }

        return ""
    }

    private fun defaultHeparinContinuousDosage(
        weight: Float,
        solutionHeparinUnits: Float,
        solutionMilliliters: Float
    ): Float {
        val patientWeight = kotlin.math.min(kotlin.math.max(weight, MIN_WEIGHT_KG), MAX_WEIGHT_KG)
        return patientWeight * DEFAULT_UNITS_PER_KG * solutionMilliliters / solutionHeparinUnits
    }

    @Suppress("ReturnCount")
    private fun getNextRemainder(
        currentAptt: Float?,
        heparinContinuousDosage: Float
    ): Instant {
        if (currentAptt == null) {
            // initial setup, measure after 4 hours
            return instantTimeProvider.now().plus(REMAINDER_FIRST_HOURS.toLong(), ChronoUnit.HOURS)
        }

        if (heparinContinuousDosage == 0f) {
            // stop heparin for one hour
            return instantTimeProvider.now().plus(REMINDER_NON_COAGULATING_HOURS.toLong(), ChronoUnit.HOURS)
        }

        return instantTimeProvider.now().plus(REMAINDER_STANDARD_HOURS.toLong(), ChronoUnit.HOURS)
    }
}
