package blue.mild.breviary.backend.services

import blue.mild.breviary.backend.db.entities.CarbohydrateIntakeValueEntity
import blue.mild.breviary.backend.db.entities.GlycemiaValueEntity
import blue.mild.breviary.backend.db.entities.InsulinDosageEntity
import blue.mild.breviary.backend.db.repositories.CarbohydrateIntakeValueRepository
import blue.mild.breviary.backend.db.repositories.GlycemiaValueRepository
import blue.mild.breviary.backend.db.repositories.InsulinDosageRepository
import blue.mild.breviary.backend.db.repositories.InsulinPatientRepository
import blue.mild.breviary.backend.db.repositories.extensions.findByIdOrThrow
import blue.mild.breviary.backend.dtos.InsulinRecommendationDtoOut
import blue.mild.breviary.backend.enums.InsulinType
import org.springframework.stereotype.Service
import java.lang.Float.max
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.transaction.Transactional
import kotlin.math.pow

const val CARBS_RATIO_NUMERATOR = 350
const val INSULIN_SENSIBILITY_NUMERATOR = 110

@Suppress("MagicNumber")
val INSULIN_EFFECT_IN_HOURS = mapOf(
    Pair(InsulinType.HUMALOG, 6),
    Pair(InsulinType.NOVORAPID, 6),
    Pair(InsulinType.APIDRA, 6),
    Pair(InsulinType.FIASP, 6),
    Pair(InsulinType.LYUMJEV, 5)
)

val EXPONENTIAL_ALMOST_ZERO_VALUE = mapOf(
    Pair(6, 1.8),
    Pair(5, 1.5)
)

/**
 * InsulinRecommendationService.
 */
@Service
class InsulinRecommendationService(
    private val insulinPatientRepository: InsulinPatientRepository,
    private val authenticationService: AuthenticationService,
    private val insulinDosageRepository: InsulinDosageRepository,
    private val glycemiaValueRepository: GlycemiaValueRepository,
    private val carbohydrateIntakeValueRepository: CarbohydrateIntakeValueRepository,
    private val instantTimeProvider: InstantTimeProvider
) {

    /**
     * Creates insulin recommendation.
     *
     * @param insulinPatientId
     * @param currentGlycemia
     * @param expectedCarbohydrateIntake
     * @return [Float]
     */
    @Transactional
    fun createInsulinRecommendation(
        insulinPatientId: Long,
        currentGlycemia: Float,
        expectedCarbohydrateIntake: Float
    ): InsulinRecommendationDtoOut {
        val insulinPatientEntity = insulinPatientRepository.findByIdOrThrow(insulinPatientId)
        val currentUser = authenticationService.getUser()

        glycemiaValueRepository.save(
            GlycemiaValueEntity(
                value = currentGlycemia,
                insulinPatient = insulinPatientEntity,
                createdBy = currentUser
            )
        )

        carbohydrateIntakeValueRepository.save(
            CarbohydrateIntakeValueEntity(
                value = expectedCarbohydrateIntake,
                insulinPatient = insulinPatientEntity,
                createdBy = currentUser
            )
        )

        val now = instantTimeProvider.now()

        @Suppress("MagicNumber")
        val latestPossibleRemainingInsulin =
            now.minus(INSULIN_EFFECT_IN_HOURS[insulinPatientEntity.insulinType]!!.toLong(), ChronoUnit.HOURS)
        val appliedDosages =
            insulinDosageRepository.getDosagesAppliedAfterDatetime(insulinPatientId, latestPossibleRemainingInsulin)

        val calculatedInsulinRecommendation = calculateInsulinRecommendation(
            tddi = insulinPatientEntity.tddi,
            targetGlycemia = insulinPatientEntity.targetGlycemia,
            currentGlycemia = currentGlycemia,
            expectedCarbohydrateIntake = expectedCarbohydrateIntake,
            insulinType = insulinPatientEntity.insulinType,
            appliedDosages = appliedDosages,
            fromDate = latestPossibleRemainingInsulin
        )

        insulinDosageRepository.save(
            InsulinDosageEntity(
                dosage = calculatedInsulinRecommendation,
                insulinPatient = insulinPatientEntity,
                createdBy = currentUser
            )
        )

        return InsulinRecommendationDtoOut(
            dosage = calculatedInsulinRecommendation
        )
    }

    private fun calculateInsulinRecommendation(
        tddi: Float,
        targetGlycemia: Float,
        currentGlycemia: Float,
        expectedCarbohydrateIntake: Float,
        insulinType: InsulinType,
        appliedDosages: Collection<InsulinDosageEntity>,
        fromDate: Instant
    ): Float {
        val dosageCarbohydrateIntake = expectedCarbohydrateIntake / (CARBS_RATIO_NUMERATOR / tddi)
        val dosageTargetGlycemia = (currentGlycemia - targetGlycemia) / (INSULIN_SENSIBILITY_NUMERATOR / tddi)
        return max(dosageCarbohydrateIntake + dosageTargetGlycemia - appliedDosages.map {
            getInsulinResiduumEffect(
                it.dosage,
                it.created,
                fromDate,
                Instant.now(),
                EXPONENTIAL_ALMOST_ZERO_VALUE[INSULIN_EFFECT_IN_HOURS[insulinType]]!!
            )
        }.sum(), 0f)
    }

    private fun getInsulinResiduumEffect(
        insulinDosage: Float,
        appliedAt: Instant,
        fromDate: Instant,
        toDate: Instant,
        exponentialAlmostZero: Double
    ): Float {
        val wholeInterval = Duration.between(fromDate, toDate).seconds.toFloat()
        val applicationInterval = Duration.between(appliedAt, toDate).seconds
        val intervalRatio = applicationInterval / wholeInterval
        return insulinDosage * kotlin.math.exp(-intervalRatio.pow(2) / (2 * exponentialAlmostZero.pow(2))).toFloat()
    }
}
