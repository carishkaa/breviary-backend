package blue.mild.breviary.backend.controllers

import blue.mild.breviary.backend.ApiRoutes
import blue.mild.breviary.backend.dtos.HeparinRecommendationDtoIn
import blue.mild.breviary.backend.dtos.HeparinRecommendationDtoOut
import blue.mild.breviary.backend.services.heparin.HeparinRecommendationService
import blue.mild.breviary.backend.utils.decodeID
import io.swagger.annotations.ApiOperation
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * HeparinRecommendationController.
 *
 * @property heparinRecommendationService [HeparinRecommendationService]
 */
@RestController
@RequestMapping("${ApiRoutes.BASE_PATH}/${ApiRoutes.HEPARIN_RECOMMENDATION}")
class HeparinRecommendationController(
    private val heparinRecommendationService: HeparinRecommendationService
) {
    /**
     * Creates heparin recommendation for particular patient from provided data.
     *
     * @param heparinRecommendation [HeparinRecommendationDtoIn]
     * @return [HeparinRecommendationDtoOut]
     */
    @ApiOperation("Creates heparin recommendation for particular patient from provided data.")
    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createHeparinRecommendation(@Valid @RequestBody heparinRecommendation: HeparinRecommendationDtoIn): HeparinRecommendationDtoOut =
        heparinRecommendationService.createHeparinRecommendation(
            heparinPatientId = heparinRecommendation.heparinPatientId.decodeID(),
            currentAptt = heparinRecommendation.currentAptt
        )
}
