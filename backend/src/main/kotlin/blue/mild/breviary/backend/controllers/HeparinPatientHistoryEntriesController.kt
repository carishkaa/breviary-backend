package blue.mild.breviary.backend.controllers

import blue.mild.breviary.backend.ApiRoutes
import blue.mild.breviary.backend.dtos.HeparinPatientHistoryEntryDtoOut
import blue.mild.breviary.backend.services.PatientHistoryEntriesService
import io.swagger.annotations.ApiOperation
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * HeparinPatientHistoryEntriesController.
 *
 * @property patientHistoryEntriesService [PatientHistoryEntriesService]
 */
@RestController
@RequestMapping("${ApiRoutes.BASE_PATH}/${ApiRoutes.HEPARIN_PATIENT_HISTORY_ENTRIES}")
class HeparinPatientHistoryEntriesController(
    private val patientHistoryEntriesService: PatientHistoryEntriesService
) {
    /**
     * Returns list of heparin patients history entries.
     *
     * @return [List<HeparinPatientHistoryEntryDtoOut>]
     */
    @ApiOperation("Returns list of heparin patients history entries.")
    @GetMapping(
        "/{heparinPatientId}",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getHeparinPatientHistoryEntries(@PathVariable heparinPatientId: Long): List<HeparinPatientHistoryEntryDtoOut> =
        patientHistoryEntriesService.getHeparinPatientHistoryEntries(heparinPatientId)
}
