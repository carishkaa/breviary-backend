package blue.mild.breviary.backend.services

import assertEquals
import blue.mild.breviary.backend.BackendApplication
import blue.mild.breviary.backend.db.entities.ApttValueEntity
import blue.mild.breviary.backend.db.entities.HeparinDosageEntity
import blue.mild.breviary.backend.db.repositories.ApttValueRepository
import blue.mild.breviary.backend.db.repositories.HeparinDosageRepository
import blue.mild.breviary.backend.db.repositories.HeparinPatientRepository
import blue.mild.breviary.backend.dtos.HeparinPatientDtoIn
import blue.mild.breviary.backend.dtos.PatientDtoIn
import blue.mild.breviary.backend.enums.Sex
import blue.mild.breviary.backend.utils.decodeID
import com.ninjasquad.springmockk.MockkBean
import io.mockk.clearAllMocks
import io.mockk.coEvery
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.time.temporal.ChronoUnit

@SpringBootTest(
    classes = [BackendApplication::class]
)
@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
class HeparinRecommendationServiceTest(
    @Autowired private val heparinRecommendationService: HeparinRecommendationService,
    @Autowired private val heparinPatientService: HeparinPatientService,
    @Autowired private val heparinDosageRepository: HeparinDosageRepository,
    @Autowired private val heparinPatientRepository: HeparinPatientRepository,
    @Autowired private val apttValueRepository: ApttValueRepository
) : ServiceTestWithDb() {

    @MockkBean(relaxUnitFun = true)
    lateinit var instantTimeProvider: InstantTimeProvider

    private fun setInstantTimeProvider(date: Instant) {
        clearAllMocks()
        coEvery { instantTimeProvider.now() } returns date
    }

    @Test
    fun `should create heparin recommendation`() {
        createAndAuthenticateUser()

        val nexReminderTime = Instant.now()
        setInstantTimeProvider(nexReminderTime)

        val heparinPatient = heparinPatientService.createHeparinPatient(
            HeparinPatientDtoIn(
                patient = PatientDtoIn(
                    firstName = "Test",
                    lastName = "Patient",
                    dateOfBirth = Instant.now(),
                    height = 180,
                    sex = Sex.FEMALE,
                    otherParams = hashMapOf()
                ),
                targetApttLow = 1.5f,
                targetApttHigh = 2f,
                solutionHeparinUnits = 25_000f,
                solutionMl = 500f,
                weight = 83f
            )
        )

        val heparinPatientEntity = heparinPatientRepository.findById(heparinPatient.id.decodeID()).get()
        val user = authenticationService.getUser()

        // Previous APTT
        apttValueRepository.save(
            ApttValueEntity(
                value = 3.6f,
                heparinPatient = heparinPatientEntity,
                createdBy = user
            )
        )

        // Previous dosage
        heparinDosageRepository.save(
            HeparinDosageEntity(
                dosageContinuous = 26f,
                dosageBolus = 26f,
                heparinPatient = heparinPatientEntity,
                createdBy = user
            )
        )

        // Current
        heparinDosageRepository.save(
            HeparinDosageEntity(
                dosageContinuous = 25f,
                dosageBolus = 25f,
                heparinPatient = heparinPatientEntity,
                createdBy = user
            )
        )

        val result = heparinRecommendationService.createHeparinRecommendation(
            heparinPatientId = heparinPatient.id.decodeID(),
            currentAptt = 3.8f
        )

        assertEquals(0f, result.actualHeparinContinuousDosage, 0.005f)
        assertEquals(25f, result.previousHeparinContinuousDosage!!, 0.005f)
        assertEquals(0f, result.actualHeparinBolusDosage, 0.005f)
        assertEquals(25f, result.previousHeparinBolusDosage!!, 0.005f)
        kotlin.test.assertEquals(nexReminderTime.plus(1, ChronoUnit.HOURS), result.nextRemainder)
        kotlin.test.assertEquals("APTT above $HIGHEST_APTT for 2 consecutive measurements.", result.doctorWarning)
    }
}
