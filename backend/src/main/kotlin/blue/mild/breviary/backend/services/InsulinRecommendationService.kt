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
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * InsulinRecommendationService.
 */
@Service
class InsulinRecommendationService(
    val insulinPatientRepository: InsulinPatientRepository,
    val authenticationService: AuthenticationService,
    val insulinDosageRepository: InsulinDosageRepository,
    val glycemiaValueRepository: GlycemiaValueRepository,
    val carbohydrateIntakeValueRepository: CarbohydrateIntakeValueRepository
) {

    /**
     * Creates insulin recommendation.
     *
     * @param insulinPatientId
     * @param currentGlycemia
     * @param expectedCarbohydrateIntake
     * @return
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

        val calculatedInsulinRecommendation = calculateInsulinRecommendation(
            tddi = insulinPatientEntity.tddi,
            targetGlycemia = insulinPatientEntity.targetGlycemia,
            currentGlycemia = currentGlycemia,
            expectedCarbohydrateIntake = expectedCarbohydrateIntake
        )

        insulinDosageRepository.save(
            InsulinDosageEntity(
                dosageInsulin = calculatedInsulinRecommendation,
                insulinPatient = insulinPatientEntity,
                createdBy = currentUser
            )
        )

        return InsulinRecommendationDtoOut(
            dosage = calculatedInsulinRecommendation
        )
    }

    @Suppress("UnusedPrivateMember", "FunctionOnlyReturningConstant") // TODO
    private fun calculateInsulinRecommendation(
        tddi: Float,
        targetGlycemia: Float,
        currentGlycemia: Float,
        expectedCarbohydrateIntake: Float
    ): Float = 0f
}
