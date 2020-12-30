package blue.mild.breviary.backend.services

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(
    classes = [
        BaseServiceTestConfig::class
    ]
)
@ActiveProfiles("test")
/**
 * Helper test annotation for classes that uses Spring DI.
 * Note @SpringBootApplication here does not work and must be specified on particular test
 * class
 */
annotation class BaseServiceTest

@Configuration
class BaseServiceTestConfig
