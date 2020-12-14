package blue.mild.breviary.backend.controllers

import blue.mild.breviary.backend.ApiRoutes
import blue.mild.breviary.backend.dtos.InsulinPatientDtoIn
import blue.mild.breviary.backend.dtos.InsulinPatientDtoOut
import blue.mild.breviary.backend.dtos.InsulinPatientWithDataDtoOut
import blue.mild.breviary.backend.dtos.PatientDtoIn
import blue.mild.breviary.backend.services.InsulinPatientService
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
 * InsulinPatientController.
 *
 * @property insulinPatientService [InsulinPatientService]
 */
@RestController
@RequestMapping("${ApiRoutes.BASE_PATH}/${ApiRoutes.INSULIN_PATIENTS}")
class InsulinPatientController(
    private val insulinPatientService: InsulinPatientService
) {
    /**
     * Gets list of active insulin patients.
     *
     * @return [List<InsulinPatientDtoOut>]
     */
    @ApiOperation("Returns list of active insulin patients.")
    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getActiveInsulinPatients(): List<InsulinPatientDtoOut> = insulinPatientService.getActiveInsulinPatients()

    /**
     * Creates new insulin patient.
     *
     * @param insulinPatient [InsulinPatientDtoIn]
     * @return [InsulinPatientDtoOut]
     */
    @ApiOperation("Creates new insulin patient.")
    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createInsulinPatient(@Valid @RequestBody insulinPatient: InsulinPatientDtoIn): InsulinPatientDtoOut =
        insulinPatientService.createInsulinPatient(insulinPatient)

    /**
     * Return insulin patient by its id.
     *
     * @param insulinPatientId [Long]
     * @return [InsulinPatientDtoOut]
     */
    @ApiOperation("Return insulin patient by its id.")
    @GetMapping(
        "/{insulinPatientId}",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getInsulinPatientById(@PathVariable insulinPatientId: Long): InsulinPatientDtoOut =
        insulinPatientService.getInsulinPatientById(insulinPatientId)

    /**
     * Return insulin patient with data by its id.
     *
     * @param insulinPatientId [Long]
     * @return [InsulinPatientWithDataDtoOut]
     */
    @ApiOperation("Return insulin patient with data by its id.")
    @GetMapping(
        "/{insulinPatientId}/with-data",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getInsulinPatientWithDataById(@PathVariable insulinPatientId: Long): InsulinPatientWithDataDtoOut =
        insulinPatientService.getInsulinPatientWithDataById(insulinPatientId)

    /**
     * Updates insulin patient with data by its id.
     *
     * @param insulinPatientId [Long]
     * @param patient [PatientDtoIn]
     * @return [InsulinPatientDtoOut]
     */
    @ApiOperation("Updates insulin patient with data by its id.")
    @PutMapping(
        "/{insulinPatientId}",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateInsulinPatient(
        @PathVariable insulinPatientId: Long,
        @Valid @RequestBody patient: PatientDtoIn
    ): InsulinPatientDtoOut =
        insulinPatientService.updateInsulinPatient(insulinPatientId, patient)
}
