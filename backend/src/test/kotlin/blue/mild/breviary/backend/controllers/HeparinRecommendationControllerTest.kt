package blue.mild.breviary.backend.controllers

import kotlin.test.assertEquals
import blue.mild.breviary.backend.ApiRoutes
import blue.mild.breviary.backend.dtos.*
import blue.mild.breviary.backend.enums.Sex
import blue.mild.breviary.backend.utils.encodeID
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import java.time.Instant
import kotlin.random.Random

class HeparinRecommendationControllerTest : ControllerTest() {

    @Test
    @DirtiesContext
    fun `should create heparin recommendation for new patient`() {
        signUpAndAuthenticateUser()

        // create new heparin patient
        val heparinPatient = executeClientPost(
            "$apiPrefix/${ApiRoutes.HEPARIN_PATIENTS}",
            setOf(HttpStatus.OK),
            typeReference<HeparinPatientDtoOut>(),
            HttpEntity<Any>(
                HeparinPatientDtoIn(
                    patient = PatientDtoIn(
                        firstName = "Test",
                        lastName = "Patient",
                        dateOfBirth = Instant.now(),
                        height = 180,
                        sex = Sex.FEMALE,
                        otherParams = hashMapOf("param1" to "x")
                    ),
                    targetApttLow = 1.5f,
                    targetApttHigh = 2f,
                    solutionHeparinUnits = 25_000f,
                    solutionMilliliters = 500f,
                    weight = 83f
                ),
                getAuthHeaders()
            )
        )

        val patient = requireNotNull(heparinPatient.body)

        // create heparin recommendation
        val heparinRecommendation = executeClientPost(
            "$apiPrefix/${ApiRoutes.HEPARIN_RECOMMENDATION}",
            setOf(HttpStatus.OK),
            typeReference<HeparinRecommendationDtoOut>(),
            HttpEntity<Any>(
                HeparinRecommendationDtoIn(
                    heparinPatientId = patient.id,
                    currentAptt = 1.1f
                ),
                getAuthHeaders()
            )
        )

        val recommendation = requireNotNull(heparinRecommendation.body)

        assertEquals(0f, recommendation.actualHeparinBolusDosage)
        assertEquals(29.88f, recommendation.actualHeparinContinuousDosage)
        assertEquals(null, recommendation.previousHeparinBolusDosage)
        assertEquals(null, recommendation.previousHeparinContinuousDosage)
        assertEquals("", recommendation.doctorWarning)
    }

    @Test
    @DirtiesContext
    fun `should not create heparin recommendation and return NOT_FOUND when patient does not exist`() {
        signUpAndAuthenticateUser()

        val invalidID = Random.nextLong(100L, 10_000L)

        // create heparin recommendation
        val response = executeClientPost(
            "$apiPrefix/${ApiRoutes.HEPARIN_RECOMMENDATION}",
            setOf(HttpStatus.NOT_FOUND),
            typeReference<ResponseDto>(),
            HttpEntity<Any>(
                HeparinRecommendationDtoIn(
                    heparinPatientId = invalidID.encodeID("heparin-patient"),
                    currentAptt = 1.1f
                ),
                getAuthHeaders()
            )
        )

        val message = requireNotNull(response.body).message

        assertEquals("HeparinPatientEntity with id $invalidID not found.", message)
    }

    @Test
    @DirtiesContext
    fun `should not create heparin recommendation and return UNAUTHORIZED when user is not signed up`() {
        executeClientPost(
            "$apiPrefix/${ApiRoutes.HEPARIN_RECOMMENDATION}",
            setOf(HttpStatus.UNAUTHORIZED),
            typeReference<ResponseDto>(),
            HttpEntity<Any>(
                HeparinRecommendationDtoIn(
                    heparinPatientId = "ID",
                    currentAptt = 1.1f
                )
            )
        )
    }

    @Test
    @DirtiesContext
    fun `should not create heparin recommendation and return BAD_REQUEST when id is empty`() {
        signUpAndAuthenticateUser()

        executeClientPost(
            "$apiPrefix/${ApiRoutes.HEPARIN_RECOMMENDATION}",
            setOf(HttpStatus.BAD_REQUEST),
            typeReference<ResponseDto>(),
            HttpEntity<Any>(
                HeparinRecommendationDtoIn(
                    heparinPatientId = "",
                    currentAptt = 1.1f
                ),
                getAuthHeaders()
            )
        )
    }
}
