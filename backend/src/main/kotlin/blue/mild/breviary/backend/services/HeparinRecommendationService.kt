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
import javax.transaction.Transactional

/**
 * HeparinRecommendationService.
 */
@Service
class HeparinRecommendationService(
    val heparinPatientRepository: HeparinPatientRepository,
    val authenticationService: AuthenticationService,
    val apttValueRepository: ApttValueRepository,
    val heparinDosageRepository: HeparinDosageRepository,
) {

    /**
     * Created heparin recommendation.
     *
     * @param heparinPatientId
     * @param currentAptt
     * @return
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
            solutionMl = heparinPatientEntity.solutionMl,
            currentContinuousDosage = currentDosage?.dosageHeparinContinuous,
            previousContinuousDosage = previousDosage?.dosageHeparinContinuous
        )

        heparinDosageRepository.save(
            HeparinDosageEntity(
                dosageHeparinContinuous = calculatedHeparinRecommendation.dosageHeparinContinuous,
                dosageHeparinBolus = calculatedHeparinRecommendation.dosageHeparinBolus,
                heparinPatient = heparinPatientEntity,
                createdBy = authenticationService.getUser()
            )
        )

        return HeparinRecommendationDtoOut(
            actualHeparinContinuousDosage = calculatedHeparinRecommendation.dosageHeparinContinuous,
            previousHeparinContinuousDosage = currentDosage?.dosageHeparinContinuous,
            actualHeparinBolusDosage = calculatedHeparinRecommendation.dosageHeparinBolus,
            previousHeparinBolusDosage = currentDosage?.dosageHeparinBolus,
            nextRemainder = calculatedHeparinRecommendation.nextRemainder,
            doctorWarning = calculatedHeparinRecommendation.doctorWarning
        )
    }

    @Suppress("UnusedPrivateMember", "LongParameterList") // TODO
    private fun calculateHeparinRecommendation(
        weight: Float,
        targetApttLow: Float,
        targetApttHigh: Float,
        currentAptt: Float?,
        previousAptt: Float?,
        solutionHeparinUnits: Float,
        solutionMl: Float,
        currentContinuousDosage: Float?,
        previousContinuousDosage: Float?
    ): HeparinRecommendationDto {
        return HeparinRecommendationDto(
            dosageHeparinContinuous = 0f,
            dosageHeparinBolus = 0f,
            nextRemainder = Instant.now(),
            doctorWarning = "TODO"
        )
    }
}
