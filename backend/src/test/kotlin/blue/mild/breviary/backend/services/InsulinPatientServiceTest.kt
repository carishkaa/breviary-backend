package blue.mild.breviary.backend.services

import blue.mild.breviary.backend.dtos.InsulinPatientDtoIn
import blue.mild.breviary.backend.dtos.InsulinPatientDtoOut
import blue.mild.breviary.backend.dtos.PatientDtoIn
import blue.mild.breviary.backend.enums.InsulinType
import blue.mild.breviary.backend.enums.Sex
import blue.mild.breviary.backend.errors.EntityNotFoundBreviaryException
import blue.mild.breviary.backend.services.insulin.InsulinPatientService
import blue.mild.breviary.backend.utils.decodeID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class InsulinPatientServiceTest(
    @Autowired private val insulinPatientService: InsulinPatientService
) : ServiceTestWithDb() {
    @Suppress("LongMethod")
    @Test
    fun `should process service CRUD operations`() {
        createAndAuthenticateUser()

        // Create patient
        val createdPatient = insulinPatientService.createInsulinPatient(
            InsulinPatientDtoIn(
                patient = PatientDtoIn(
                    firstName = "Insulin",
                    lastName = "Patient",
                    dateOfBirth = Instant.now(),
                    height = 158,
                    sex = Sex.MALE,
                    otherParams = hashMapOf(Pair("key", "value"))
                ),
                targetGlycemia = 4.5f,
                tddi = 500f,
                insulinType = InsulinType.HUMALOG
            )
        )

        // Get patient
        val getPatientById = insulinPatientService.getInsulinPatientById(createdPatient.id.decodeID())
        assertInsulinPatientEquals(createdPatient, getPatientById)

        // Get patient with data
        val getPatientByIdWithData = insulinPatientService.getInsulinPatientWithDataById(createdPatient.id.decodeID())
        assertInsulinPatientEquals(createdPatient, getPatientByIdWithData.insulinPatient)

        // Update patient
        val updatedPatient = insulinPatientService.updateInsulinPatient(
            insulinPatientId = getPatientById.id.decodeID(),
            patient = PatientDtoIn(
                firstName = "Insulin2",
                lastName = "Patient2",
                dateOfBirth = Instant.now(),
                height = 160,
                sex = Sex.FEMALE,
                otherParams = hashMapOf(Pair("key2", "value2"))
            )
        )

        val getUpdatedPatientById = insulinPatientService.getInsulinPatientById(createdPatient.id.decodeID())
        assertInsulinPatientEquals(updatedPatient, getUpdatedPatientById)

        // Get active patients
        val activePatients = insulinPatientService.getActiveInsulinPatients()
        assertEquals(1, activePatients.size)
        assertInsulinPatientEquals(updatedPatient, activePatients[0])

        // Get non-existing patient
        assertFailsWith<EntityNotFoundBreviaryException> {
            insulinPatientService.getInsulinPatientById(7)
        }

        // Get non-existing patient with data
        assertFailsWith<EntityNotFoundBreviaryException> {
            insulinPatientService.getInsulinPatientWithDataById(7)
        }

        // Update non-existing patient
        assertFailsWith<EntityNotFoundBreviaryException> {
            insulinPatientService.updateInsulinPatient(
                insulinPatientId = 7,
                patient = PatientDtoIn(
                    firstName = "Insulin2",
                    lastName = "Patient2",
                    dateOfBirth = Instant.now(),
                    height = 160,
                    sex = Sex.FEMALE,
                    otherParams = hashMapOf(Pair("key2", "value2"))
                )
            )
        }
    }

    private fun assertInsulinPatientEquals(expected: InsulinPatientDtoOut, actual: InsulinPatientDtoOut) {
        assertEquals(expected.id, actual.id)
        assertEquals(expected.patient.dateOfBirth, actual.patient.dateOfBirth)
        assertEquals(expected.patient.firstName, actual.patient.firstName)
        assertEquals(expected.patient.height, actual.patient.height)
        assertEquals(expected.patient.id, actual.patient.id)
        assertEquals(expected.patient.lastName, actual.patient.lastName)
        assertEquals(expected.patient.otherParams, actual.patient.otherParams)
        assertEquals(expected.patient.sex, actual.patient.sex)
        assertEquals(expected.targetGlycemia, actual.targetGlycemia)
        assertEquals(expected.tddi, actual.tddi)
    }
}
