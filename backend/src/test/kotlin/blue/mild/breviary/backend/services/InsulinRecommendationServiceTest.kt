package blue.mild.breviary.backend.services

import assertEquals
import blue.mild.breviary.backend.BackendApplication
import blue.mild.breviary.backend.dtos.InsulinPatientDtoIn
import blue.mild.breviary.backend.dtos.PatientDtoIn
import blue.mild.breviary.backend.enums.InsulinType
import blue.mild.breviary.backend.enums.Sex
import blue.mild.breviary.backend.utils.decodeID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant

@SpringBootTest(
    classes = [BackendApplication::class]
)
@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
class InsulinRecommendationServiceTest(
    @Autowired private val insulinRecommendationService: InsulinRecommendationService,
    @Autowired private val patientService: InsulinPatientService
) : ServiceTestWithDb() {
    @Test
    fun `should create insulin recommendation`() {
        createAndAuthenticateUser()

        val insulinPatient = patientService.createInsulinPatient(
            InsulinPatientDtoIn(
                patient = PatientDtoIn(
                    firstName = "Test",
                    lastName = "Patient",
                    dateOfBirth = Instant.now(),
                    height = 180,
                    sex = Sex.FEMALE,
                    otherParams = hashMapOf()
                ),
                targetGlycemia = 8f,
                tddi = 50f,
                insulinType = InsulinType.APIDRA
            )
        )

        val result = insulinRecommendationService.createInsulinRecommendation(
            insulinPatientId = insulinPatient.id.decodeID(),
            currentGlycemia = 10f,
            expectedCarbohydrateIntake = 70f
        )

        assertEquals(10.909091f, result.dosage, 0.005f)
    }
}
