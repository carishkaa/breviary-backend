package blue.mild.breviary.backend.services

import blue.mild.breviary.backend.App
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(
    classes = [
        BaseServiceTestWithDbConfig::class
    ]
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
/**
 * Helper test annotation for classes that uses Spring DI.
 * Note @SpringBootApplication here does not work and must be specified on particular test
 * class
 */
annotation class BaseServiceTestWithDb

@Configuration
class BaseServiceTestWithDbConfig {
    @Bean
    @Primary
    @ConfigurationProperties(prefix = App.DB_PREFIX)
    fun dataSourceProperties(): DataSourceProperties = DataSourceProperties()
}
