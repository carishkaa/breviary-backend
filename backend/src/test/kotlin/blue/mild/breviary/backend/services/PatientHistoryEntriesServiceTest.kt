package blue.mild.breviary.backend.services

import blue.mild.breviary.backend.BackendApplication
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(
    classes = [BackendApplication::class]
)
@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
class PatientHistoryEntriesServiceTest : ServiceTestWithDb() {
    // TODO
}
