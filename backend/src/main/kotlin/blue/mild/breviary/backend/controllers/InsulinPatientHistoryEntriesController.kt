package blue.mild.breviary.backend.controllers

import blue.mild.breviary.backend.ApiRoutes
import blue.mild.breviary.backend.dtos.InsulinPatientHistoryEntryDtoOut
import blue.mild.breviary.backend.services.PatientHistoryEntriesService
import blue.mild.breviary.backend.utils.decodeID
import io.swagger.annotations.ApiOperation
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * InsulinPatientHistoryEntriesController.
 *
 * @property patientHistoryEntriesService [PatientHistoryEntriesService]
 */
@RestController
@RequestMapping("${ApiRoutes.BASE_PATH}/${ApiRoutes.INSULIN_PATIENTS_HISTORY_ENTRIES}")
class InsulinPatientHistoryEntriesController(
    private val patientHistoryEntriesService: PatientHistoryEntriesService
) {
    /**
     * Returns list of insulin patients history entries.
     *
     * @param heparinPatientId [String]
     *
     * @return [List<InsulinPatientHistoryEntryDtoOut>]
     */
    @ApiOperation("Returns list of insulin patients history entries.")
    @GetMapping(
        "/{insulinPatientId}",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getInsulinPatientHistoryEntries(@PathVariable insulinPatientId: String): List<InsulinPatientHistoryEntryDtoOut> =
        patientHistoryEntriesService.getInsulinPatientHistoryEntries(insulinPatientId.decodeID())
}
