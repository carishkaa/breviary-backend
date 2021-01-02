package blue.mild.breviary.backend.controllers

import blue.mild.breviary.backend.ApiRoutes
import blue.mild.breviary.backend.dtos.ApplicationInfoDto
import io.swagger.annotations.ApiOperation
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * VersionController.
 *
 * @property applicationInfoDto [ApplicationInfoDto]
 */
@RestController
@RequestMapping("${ApiRoutes.BASE_PATH}/${ApiRoutes.VERSION}")
class VersionController(private val applicationInfoDto: ApplicationInfoDto) {
    /**
     * Get application version info.
     */
    @ApiOperation("Get application version info.")
    @GetMapping("", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getVersion(): ApplicationInfoDto = applicationInfoDto
}
