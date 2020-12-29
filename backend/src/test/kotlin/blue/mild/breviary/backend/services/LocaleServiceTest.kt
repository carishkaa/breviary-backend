package blue.mild.breviary.backend.services

import blue.mild.breviary.backend.BackendApplication
import blue.mild.breviary.backend.config.LocalizationConfig
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.servlet.LocaleResolver
import javax.servlet.http.HttpServletRequest
import kotlin.test.assertEquals

@SpringBootTest(
    classes = [BackendApplication::class]
)
@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
class LocaleServiceTest(
    @Autowired private val localeResolver: LocaleResolver,
    @Autowired private val localeService: LocaleService
) {

    @Test
    fun `should get localized message by code`() {
        val enRequest = Mockito.mock(HttpServletRequest::class.java)
        `when`(enRequest.getHeader(LocalizationConfig.HEADER_NAME)).thenReturn("en")
        localeResolver.resolveLocale(enRequest)
        val enMessage = localeService.getMessage("lang.en")
        assertEquals("English", enMessage)
    }
}
