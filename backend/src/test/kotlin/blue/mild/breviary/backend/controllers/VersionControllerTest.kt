package blue.mild.breviary.backend.controllers

import blue.mild.breviary.backend.ApiRoutes
import blue.mild.breviary.backend.dtos.VersionInfoDtoOut
import org.junit.jupiter.api.Test
import org.springframework.core.io.Resource
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import java.util.Properties
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class VersionControllerTest : ControllerTest() {

    @Value("classpath:version.properties")
    lateinit var versionResource: Resource

    @Test
    @DirtiesContext
    fun `should get app version info (public)`() {

        val versionResult = executeClientGet(
            "$apiPrefix/${ApiRoutes.VERSION}",
            setOf(HttpStatus.OK),
            typeReference<VersionInfoDtoOut>()
        )

        assertNotNull(versionResult.body)

        val properties = Properties().apply {
            load(versionResource.inputStream)
        }

        assertEquals(properties.getProperty("app"), versionResult.body!!.app)
    }
}
