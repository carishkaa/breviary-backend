package blue.mild.breviary.backend.controllers

import blue.mild.breviary.backend.ApiRoutes
import blue.mild.breviary.backend.dtos.ApplicationInfoDto
import blue.mild.breviary.backend.services.ApplicationInfoService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class VersionControllerTest : ControllerTest() {

    @Autowired
    lateinit var appInfoService: ApplicationInfoService

    @Test
    @DirtiesContext
    fun `should get app version info (public)`() {

        val versionResult = executeClientGet(
            "$apiPrefix/${ApiRoutes.VERSION}",
            setOf(HttpStatus.OK),
            typeReference<ApplicationInfoDto>()
        )
        val body = versionResult.body
        assertNotNull(body)

        val appInfo = appInfoService.getApplicationInfo()
        assertEquals(appInfo.version, body.version)
        assertEquals(appInfo.environment, body.environment)
    }
}
