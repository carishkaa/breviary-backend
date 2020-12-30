package blue.mild.breviary.backend.services

import blue.mild.breviary.backend.BackendApplication
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    classes = [BackendApplication::class]
)
@BaseServiceTest
open class ServiceTest
