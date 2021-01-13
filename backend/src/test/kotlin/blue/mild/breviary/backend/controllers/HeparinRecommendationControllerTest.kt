package blue.mild.breviary.backend.controllers

import blue.mild.breviary.backend.ApiRoutes
import blue.mild.breviary.backend.dtos.*
import blue.mild.breviary.backend.enums.Sex
import blue.mild.breviary.backend.services.heparin.HeparinPatientService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import java.time.Instant
import kotlin.test.assertNotNull

class HeparinRecommendationControllerTest(
    @Autowired private val heparinPatientService: HeparinPatientService
) : ControllerTest() {

    // OK
    @Test
    @DirtiesContext
    fun `should create heparin recommendation`(){
        signup()
        authenticateUser()

        val heparinPatient = heparinPatientService.createHeparinPatient(
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
            )
        )

        val heparinRecommendation = executeClientPost(
            "$apiPrefix/${ApiRoutes.HEPARIN_RECOMMENDATION}",
            setOf(HttpStatus.OK),
            typeReference<HeparinRecommendationDtoOut>(),
            HttpEntity<Any>(
                HeparinRecommendationDtoIn(
                    heparinPatientId = heparinPatient.id,
                    currentAptt = 1.1f
                ),
                getAuthHeaders()
            )
        )

        assertNotNull(heparinRecommendation.body)
        assert(heparinRecommendation.body!!.actualHeparinBolusDosage == 0.0f)
    }


    // FAILED (json parse errors)
    @Test
    @DirtiesContext
    fun `should create heparin recommendation for new patient`(){
        signup()
        authenticateUser()

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

        // create heparin recommendation
        executeClientPost(
            "$apiPrefix/${ApiRoutes.HEPARIN_RECOMMENDATION}",
            setOf(HttpStatus.OK),
            typeReference<HeparinRecommendationDtoOut>(),
            HttpEntity<Any>(
                HeparinRecommendationDtoIn(
                    heparinPatientId = heparinPatient.body!!.id,
                    currentAptt = 1.1f
                ),
                getAuthHeaders()
            )
        )
    }


    // FAILED (json parse error)
    @Test
    @DirtiesContext
    fun `should not create heparin recommendation when patient does not exist`(){
        signup()
        authenticateUser()

        val invalidID = "MzB8aGVwYXJpbi1wYXRpZW50"

        // create heparin recommendation
        executeClientPost(
            "$apiPrefix/${ApiRoutes.HEPARIN_RECOMMENDATION}",
            setOf(HttpStatus.BAD_REQUEST),
            typeReference<HeparinRecommendationDtoOut>(),
            HttpEntity<Any>(
                HeparinRecommendationDtoIn(
                    heparinPatientId = invalidID,
                    currentAptt = 1.1f
                ),
                getAuthHeaders()
            )
        )
    }
}


