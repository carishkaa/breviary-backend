package blue.mild.breviary.backend.controllers

import blue.mild.breviary.backend.ApiRoutes
import blue.mild.breviary.backend.dtos.VersionInfoDtoOut
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * VersionController.
 *
 * @property versionInfoDtoOut [VersionInfoDtoOut]
 */
@RestController
@RequestMapping("${ApiRoutes.BASE_PATH}/${ApiRoutes.VERSION}")
class VersionController(@Qualifier("VersionInfo") private val versionInfoDtoOut: VersionInfoDtoOut) {
    /**
     * Get application version info.
     */
    @ApiOperation("Get application version info.")
    @GetMapping("", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getVersion(): VersionInfoDtoOut = versionInfoDtoOut
}
