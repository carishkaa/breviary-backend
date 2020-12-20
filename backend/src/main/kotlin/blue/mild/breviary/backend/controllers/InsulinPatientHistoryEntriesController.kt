package blue.mild.breviary.backend.controllers

import blue.mild.breviary.backend.ApiRoutes
import blue.mild.breviary.backend.dtos.InsulinPatientHistoryEntryDtoOut
import blue.mild.breviary.backend.services.PatientHistoryEntriesService
import io.swagger.annotations.ApiOperation
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * InsulinPatientHistoryEntriesController.
 *
 * @property patientHistoryEntriesService [PatientHistoryEntriesService]
 */
@RestController
@RequestMapping("${ApiRoutes.BASE_PATH}/${ApiRoutes.HEPARIN_PATIENT_HISTORY_ENTRIES}")
class InsulinPatientHistoryEntriesController(
    private val patientHistoryEntriesService: PatientHistoryEntriesService
) {
    /**
     * Returns list of insulin patients history entries.
     *
     * @return [List<InsulinPatientHistoryEntryDtoOut>]
     */
    @ApiOperation("Returns list of insulin patients history entries.")
    @GetMapping(
        "/{insulinPatientId}",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getInsulinPatientHistoryEntries(insulinPatientId: Long): List<InsulinPatientHistoryEntryDtoOut> =
        patientHistoryEntriesService.getInsulinPatientHistoryEntries(insulinPatientId)
}
