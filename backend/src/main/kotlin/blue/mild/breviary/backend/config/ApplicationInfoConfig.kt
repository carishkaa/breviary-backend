package blue.mild.breviary.backend.config

import blue.mild.breviary.backend.dtos.ApplicationInfoDto
import blue.mild.breviary.backend.services.ApplicationInfoService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuration for ApplicationInfoDto.
 */
@Configuration
class ApplicationInfoConfig(
    private val infoService: ApplicationInfoService
) {
    /**
     * Provides version of the application.
     */
    @Bean
    fun createVersionInfo(): ApplicationInfoDto = infoService.getApplicationInfo()
}
