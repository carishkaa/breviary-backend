package blue.mild.breviary.backend.controllers

import blue.mild.breviary.backend.ApiRoutes
import blue.mild.breviary.backend.dtos.HeparinPatientDtoIn
import blue.mild.breviary.backend.dtos.HeparinPatientDtoOut
import blue.mild.breviary.backend.dtos.HeparinPatientWithDataDtoOut
import blue.mild.breviary.backend.dtos.PatientDtoIn
import blue.mild.breviary.backend.services.HeparinPatientService
import io.swagger.annotations.ApiOperation
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * HeparinPatientController.
 *
 * @property heparinPatientService [HeparinPatientService]
 */
@RestController
@RequestMapping("${ApiRoutes.BASE_PATH}/${ApiRoutes.HEPARIN_PATIENTS}")
class HeparinPatientController(
    private val heparinPatientService: HeparinPatientService,
) {
    /**
     * Gets list of active heparin patients.
     *
     * @return [List<HeparinPatientDtoOut>]
     */
    @ApiOperation("Returns list of active heparin patients.")
    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getActiveHeparinPatients(): List<HeparinPatientDtoOut> = heparinPatientService.getActiveHeparinPatients()

    /**
     * Creates new heparin patient.
     *
     * @param heparinPatient [HeparinPatientDtoIn]
     * @return [HeparinPatientDtoOut]
     */
    @ApiOperation("Creates new heparin patient.")
    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createHeparinPatient(@Valid @RequestBody heparinPatient: HeparinPatientDtoIn): HeparinPatientDtoOut =
        heparinPatientService.createHeparinPatient(heparinPatient)

    /**
     * Return heparin patient by its id.
     *
     * @param heparinPatientId [Long]
     * @return [HeparinPatientDtoOut]
     */
    @ApiOperation("Return heparin patient by its id.")
    @GetMapping(
        "/{heparinPatientId}",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getHeparinPatientById(@PathVariable heparinPatientId: Long): HeparinPatientDtoOut =
        heparinPatientService.getHeparinPatientById(heparinPatientId)

    /**
     * Return heparin patient with data by its id.
     *
     * @param heparinPatientId [Long]
     * @return [HeparinPatientWithDataDtoOut]
     */
    @ApiOperation("Return heparin patient with data by its id.")
    @GetMapping(
        "/{heparinPatientId}/with-data",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getHeparinPatientWithDataById(@PathVariable heparinPatientId: Long): HeparinPatientWithDataDtoOut =
        heparinPatientService.getHeparinPatientWithDataById(heparinPatientId)

    /**
     * Updates heparin patient with data by its id.
     *
     * @param heparinPatientId [Long]
     * @param patient [PatientDtoIn]
     * @return [HeparinPatientDtoOut]
     */
    @ApiOperation("Updates heparin patient with data by its id.")
    @PutMapping(
        "/{heparinPatientId}",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateHeparinPatient(
        @PathVariable heparinPatientId: Long,
        @Valid @RequestBody patient: PatientDtoIn
    ): HeparinPatientDtoOut =
        heparinPatientService.updateHeparinPatient(heparinPatientId, patient)
}
