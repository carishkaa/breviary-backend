package blue.mild.breviary.backend.config

import blue.mild.breviary.backend.dtos.VersionInfoDtoOut
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import java.util.Properties

/**
 * UserDataConfig.
 *
 * @property versionResource
 */
@Configuration
class UserDataConfig(
    @Value("classpath:version.properties") private val versionResource: Resource
) {

    private val properties = Properties().apply {
        load(versionResource.inputStream)
    }

    /**
     * Creates version.
     *
     */
    @Bean("VersionInfo")
    fun createVersionInfo() = VersionInfoDtoOut(
        app = properties.getProperty("app")
    )
}
