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

class HeparinRecommendationControllerTest(
    @Autowired private val heparinPatientService: HeparinPatientService
) : ControllerTest() {

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
                    otherParams = hashMapOf()
                ),
                targetApttLow = 1.5f,
                targetApttHigh = 2f,
                solutionHeparinUnits = 25_000f,
                solutionMilliliters = 500f,
                weight = 83f
            )
        )

        executeClientPost(
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

    }
}


