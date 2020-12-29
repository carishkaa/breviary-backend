package blue.mild.breviary.backend.controllers

import blue.mild.breviary.backend.ApiRoutes
import blue.mild.breviary.backend.dtos.InsulinRecommendationDtoIn
import blue.mild.breviary.backend.dtos.InsulinRecommendationDtoOut
import blue.mild.breviary.backend.services.insulin.InsulinRecommendationService
import blue.mild.breviary.backend.utils.decodeID
import io.swagger.annotations.ApiOperation
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * InsulinRecommendationController.
 *
 * @property insulinRecommendationService [InsulinRecommendationService]
 */
@RestController
@RequestMapping("${ApiRoutes.BASE_PATH}/${ApiRoutes.INSULIN_RECOMMENDATION}")
class InsulinRecommendationController(
    private val insulinRecommendationService: InsulinRecommendationService
) {
    /**
     * Creates insulin recommendation for particular patient from provided data.
     *
     * @param insulinRecommendation [InsulinRecommendationDtoIn]
     * @return [InsulinRecommendationDtoOut]
     */
    @ApiOperation("Creates heparin recommendation for particular patient from provided data.")
    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createInsulinRecommendation(@Valid @RequestBody insulinRecommendation: InsulinRecommendationDtoIn): InsulinRecommendationDtoOut =
        insulinRecommendationService.createInsulinRecommendation(
            insulinPatientId = insulinRecommendation.insulinPatientId.decodeID(),
            currentGlycemia = insulinRecommendation.currentGlycemia,
            expectedCarbohydrateIntake = insulinRecommendation.expectedCarbohydrateIntake
        )
}
