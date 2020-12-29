package blue.mild.breviary.backend.services

import blue.mild.breviary.backend.dtos.HeparinPatientDtoIn
import blue.mild.breviary.backend.dtos.HeparinPatientDtoOut
import blue.mild.breviary.backend.dtos.PatientDtoIn
import blue.mild.breviary.backend.enums.Sex
import blue.mild.breviary.backend.errors.EntityNotFoundBreviaryException
import blue.mild.breviary.backend.utils.decodeID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class HeparinPatientServiceTest(
    @Autowired private val heparinPatientService: HeparinPatientService
) : ServiceTestWithDb() {
    @Suppress("LongMethod")
    @Test
    fun `should process service CRUD operations`() {
        createAndAuthenticateUser()

        // Create patient
        val createdPatient = heparinPatientService.createHeparinPatient(
            HeparinPatientDtoIn(
                patient = PatientDtoIn(
                    firstName = "Heparin",
                    lastName = "Patient",
                    dateOfBirth = Instant.now(),
                    height = 158,
                    sex = Sex.MALE,
                    otherParams = hashMapOf(Pair("key", "value"))
                ),
                targetApttLow = 4.5f,
                targetApttHigh = 500f,
                solutionHeparinUnits = 600f,
                solutionMilliliters = 10f,
                weight = 98f
            )
        )

        // Get patient
        val getPatientById = heparinPatientService.getHeparinPatientById(createdPatient.id.decodeID())
        assertHeparinPatientEquals(createdPatient, getPatientById)

        // Get patient with data
        val getPatientByIdWithData = heparinPatientService.getHeparinPatientWithDataById(createdPatient.id.decodeID())
        assertHeparinPatientEquals(createdPatient, getPatientByIdWithData.heparinPatient)

        // Update patient
        val updatedPatient = heparinPatientService.updateHeparinPatient(
            heparinPatientId = getPatientById.id.decodeID(),
            patient = PatientDtoIn(
                firstName = "Heparin2",
                lastName = "Patient2",
                dateOfBirth = Instant.now(),
                height = 160,
                sex = Sex.FEMALE,
                otherParams = hashMapOf(Pair("key2", "value2"))
            )
        )

        val getUpdatedPatientById = heparinPatientService.getHeparinPatientById(createdPatient.id.decodeID())
        assertHeparinPatientEquals(updatedPatient, getUpdatedPatientById)

        // Get active patients
        val activePatients = heparinPatientService.getActiveHeparinPatients()
        assertEquals(1, activePatients.size)
        assertHeparinPatientEquals(updatedPatient, activePatients[0])

        // Get non-existing patient
        assertFailsWith<EntityNotFoundBreviaryException> {
            heparinPatientService.getHeparinPatientById(7)
        }

        // Get non-existing patient with data
        assertFailsWith<EntityNotFoundBreviaryException> {
            heparinPatientService.getHeparinPatientWithDataById(7)
        }

        // Update non-existing patient
        assertFailsWith<EntityNotFoundBreviaryException> {
            heparinPatientService.updateHeparinPatient(
                heparinPatientId = 7,
                patient = PatientDtoIn(
                    firstName = "Heparin2",
                    lastName = "Patient2",
                    dateOfBirth = Instant.now(),
                    height = 160,
                    sex = Sex.FEMALE,
                    otherParams = hashMapOf(Pair("key2", "value2"))
                )
            )
        }
    }

    private fun assertHeparinPatientEquals(expected: HeparinPatientDtoOut, actual: HeparinPatientDtoOut) {
        assertEquals(expected.id, actual.id)
        assertEquals(expected.patient.dateOfBirth, actual.patient.dateOfBirth)
        assertEquals(expected.patient.firstName, actual.patient.firstName)
        assertEquals(expected.patient.height, actual.patient.height)
        assertEquals(expected.patient.id, actual.patient.id)
        assertEquals(expected.patient.lastName, actual.patient.lastName)
        assertEquals(expected.patient.otherParams, actual.patient.otherParams)
        assertEquals(expected.patient.sex, actual.patient.sex)
        assertEquals(expected.targetApttLow, actual.targetApttLow)
        assertEquals(expected.targetApttHigh, actual.targetApttHigh)
        assertEquals(expected.solutionHeparinUnits, actual.solutionHeparinUnits)
        assertEquals(expected.solutionMilliliters, actual.solutionMilliliters)
        assertEquals(expected.weight, actual.weight)
    }
}
