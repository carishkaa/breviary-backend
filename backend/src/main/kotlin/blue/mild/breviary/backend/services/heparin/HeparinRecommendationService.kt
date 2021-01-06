package blue.mild.breviary.backend.services.heparin

import blue.mild.breviary.backend.db.entities.ApttValueEntity
import blue.mild.breviary.backend.db.entities.HeparinDosageEntity
import blue.mild.breviary.backend.db.repositories.ApttValueRepository
import blue.mild.breviary.backend.db.repositories.HeparinDosageRepository
import blue.mild.breviary.backend.db.repositories.HeparinPatientRepository
import blue.mild.breviary.backend.db.repositories.extensions.findByIdOrThrow
import blue.mild.breviary.backend.dtos.HeparinRecommendationDto
import blue.mild.breviary.backend.dtos.HeparinRecommendationDtoOut
import blue.mild.breviary.backend.services.AuthenticationService
import blue.mild.breviary.backend.services.InstantTimeProvider
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.transaction.Transactional
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Helper data structure for heparin recommendation.
 *
 * @property heparinContinuousDosage
 * @property heparinBolusDosage
 */
data class RecommendedHeparinDosage(val heparinContinuousDosage: Float, val heparinBolusDosage: Float)

/**
 * HeparinRecommendationService.
 * TODO this is a crucial service, we need to add a lot of documentation why do we do what
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

    @Suppress("LongParameterList", "LongMethod", "ComplexMethod")
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
        // TODO rewrite that to multiple methods and to when switch
        return if (currentAptt == null || currentContinuousDosage == null) {
            // initial setup, no measurements yet
            RecommendedHeparinDosage(
                defaultHeparinContinuousDosage(
                    weight = weight,
                    solutionHeparinUnits = solutionHeparinUnits,
                    solutionMilliliters = solutionMilliliters
                ),
                heparinBolusDosage = 0f
            )
        } else if (currentContinuousDosage == 0f) {
            RecommendedHeparinDosage(
                getNewDosage(
                    weight = weight,
                    currentDosage = requireNotNull(previousContinuousDosage) {
                        "previousContinuousDosage was null even though currentContinuousDosage is 0!"
                    },
                    solutionHeparinUnits = solutionHeparinUnits,
                    solutionMilliliters = solutionMilliliters,
                    unitsPerKg = HIGHEST_APTT_DOSAGE_PER_KG_CHANGE
                ),
                heparinBolusDosage = 0f
            )
        } else if (currentAptt < LOWEST_APTT) {
            RecommendedHeparinDosage(
                getNewDosage(
                    weight = weight,
                    currentDosage = currentContinuousDosage,
                    solutionHeparinUnits = solutionHeparinUnits,
                    solutionMilliliters = solutionMilliliters,
                    unitsPerKg = LOWEST_APTT_DOSAGE_PER_KG_CHANGE
                ),
                calculateBolus(
                    weight = weight,
                    solutionHeparinUnits = solutionHeparinUnits,
                    solutionMilliliters = solutionMilliliters,
                    unitsPerKg = LOWEST_APTT_BOLUS
                )
            )
        } else if (currentAptt < LOW_APTT) {
            RecommendedHeparinDosage(
                getNewDosage(
                    weight = weight,
                    currentDosage = currentContinuousDosage,
                    solutionHeparinUnits = solutionHeparinUnits,
                    solutionMilliliters = solutionMilliliters,
                    unitsPerKg = LOW_APTT_DOSAGE_PER_KG_CHANGE
                ),
                calculateBolus(
                    weight = weight,
                    solutionHeparinUnits = solutionHeparinUnits,
                    solutionMilliliters = solutionMilliliters,
                    unitsPerKg = LOW_APTT_BOLUS
                )
            )
        } else if (currentAptt < targetApttLow) {
            RecommendedHeparinDosage(
                getNewDosage(
                    weight = weight,
                    currentDosage = currentContinuousDosage,
                    solutionHeparinUnits = solutionHeparinUnits,
                    solutionMilliliters = solutionMilliliters,
                    unitsPerKg = BELOW_STANDARD_APTT_DOSAGE_PER_KG_CHANGE
                ),
                heparinBolusDosage = 0f
            )
        } else if (currentAptt < targetApttHigh) {
            RecommendedHeparinDosage(currentContinuousDosage, 0f)
        } else if (currentAptt < STANDARD_APTT) {
            RecommendedHeparinDosage(
                getNewDosage(
                    weight = weight,
                    currentDosage = currentContinuousDosage,
                    solutionHeparinUnits = solutionHeparinUnits,
                    solutionMilliliters = solutionMilliliters,
                    unitsPerKg = ABOVE_STANDARD_APTT_DOSAGE_PER_KG_CHANGE
                ),
                heparinBolusDosage = 0f
            )
        } else if (currentAptt <= HIGH_APTT) {
            RecommendedHeparinDosage(
                getNewDosage(
                    weight = weight,
                    currentDosage = currentContinuousDosage,
                    solutionHeparinUnits = solutionHeparinUnits,
                    solutionMilliliters = solutionMilliliters,
                    unitsPerKg = HIGH_APTT_DOSAGE_PER_KG_CHANGE
                ),
                heparinBolusDosage = 0f
            )
        } else {
            RecommendedHeparinDosage(0f, 0f)
        }
    }

    private fun calculateBolus(
        weight: Float,
        solutionHeparinUnits: Float,
        solutionMilliliters: Float,
        unitsPerKg: Float
    ): Float = unitsPerKg * weight * solutionMilliliters / solutionHeparinUnits

    @Suppress("UnnecessaryParentheses") // we want to be as explicit as possible
    private fun getNewDosage(
        weight: Float,
        currentDosage: Float,
        solutionHeparinUnits: Float,
        solutionMilliliters: Float,
        unitsPerKg: Float
    ): Float =
        currentDosage + (unitsPerKg * weight * solutionMilliliters / solutionHeparinUnits)

    @Suppress("LongParameterList")
    /**
     * @param weight in kg, stored in database
     * @param targetApttLow set by the doctor, stored in database
     * @param targetApttHigh set by the doctor, stored in database
     * @param currentAptt measured before the calculation
     * @param previousAptt result of previous measurement, stored in database
     * @param solutionHeparinUnits used to calculate heparin concentration, stored in database
     * @param solutionMilliliters used to calculate heparin concentration, stored in database
     * @param currentContinuousDosage amount of heparin solution the patient is currently receiving
     * @param previousContinuousDosage amount of heparin solution the patient was receiving before the previous measurement
     */
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
            weight = weight,
            targetApttLow = targetApttLow,
            targetApttHigh = targetApttHigh,
            currentAptt = currentAptt,
            solutionHeparinUnits = solutionHeparinUnits,
            solutionMilliliters = solutionMilliliters,
            currentContinuousDosage = currentContinuousDosage,
            previousContinuousDosage = previousContinuousDosage
        )

        val nextRemainder = getNextRemainder(
            currentAptt = currentAptt,
            heparinContinuousDosage = recommendedHeparinDosage.heparinContinuousDosage
        )

        val doctorWarning = getDoctorWarning(
            weight = weight,
            currentAptt = currentAptt,
            previousAptt = previousAptt,
            solutionHeparinUnits = solutionHeparinUnits,
            solutionMilliliters = solutionMilliliters,
            heparinContinuousDosage = recommendedHeparinDosage.heparinContinuousDosage
        )

        return HeparinRecommendationDto(
            dosageContinuous = recommendedHeparinDosage.heparinContinuousDosage,
            dosageBolus = recommendedHeparinDosage.heparinBolusDosage,
            nextRemainder = nextRemainder,
            doctorWarning = doctorWarning
        )
    }

    @Suppress("ComplexMethod")
    private fun getDoctorWarning(
        currentAptt: Float?,
        previousAptt: Float?,
        heparinContinuousDosage: Float,
        weight: Float,
        solutionHeparinUnits: Float,
        solutionMilliliters: Float
    ): String {
        val dosageDiff = abs(
            heparinContinuousDosage - defaultHeparinContinuousDosage(
                weight = weight,
                solutionHeparinUnits = solutionHeparinUnits,
                solutionMilliliters = solutionMilliliters
            )
        )

        return when {
            currentAptt == null || previousAptt == null -> ""

            currentAptt < LOWEST_APTT && previousAptt < LOWEST_APTT ->
                "APTT below $LOWEST_APTT for 2 consecutive measurements."

            currentAptt > HIGHEST_APTT && previousAptt > HIGHEST_APTT ->
                "APTT above $HIGHEST_APTT for 2 consecutive measurements."

            dosageDiff >= EXTREME_DOSAGE_DIFF && heparinContinuousDosage > 0 ->
                "Current continuous heparin dosage differs from default weight " +
                        "based dosage by ${dosageDiff.roundToInt()}."

            else -> ""
        }
    }

    private fun defaultHeparinContinuousDosage(
        weight: Float,
        solutionHeparinUnits: Float,
        solutionMilliliters: Float
    ): Float {
        val patientWeight = min(max(weight, MIN_WEIGHT_KG), MAX_WEIGHT_KG)
        return patientWeight * DEFAULT_UNITS_PER_KG * solutionMilliliters / solutionHeparinUnits
    }

    private fun getNextRemainder(
        currentAptt: Float?,
        heparinContinuousDosage: Float
    ): Instant = when {
        // initial setup, measure after 4 hours
        currentAptt == null ->
            instantTimeProvider.now().plus(REMAINDER_FIRST_HOURS.toLong(), ChronoUnit.HOURS)
        // stop heparin for one hour
        heparinContinuousDosage == 0f ->
            instantTimeProvider.now().plus(REMINDER_NON_COAGULATING_HOURS.toLong(), ChronoUnit.HOURS)
        // TODO what is this case?
        else ->
            instantTimeProvider.now().plus(REMAINDER_STANDARD_HOURS.toLong(), ChronoUnit.HOURS)
    }
}
