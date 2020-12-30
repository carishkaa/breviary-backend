package blue.mild.breviary.backend.controllers

import Constants.TEST_FIRST_NAME
import Constants.TEST_LAST_NAME
import Constants.TEST_PASSWORD
import Constants.TEST_USERNAME
import blue.mild.breviary.backend.ApiRoutes
import blue.mild.breviary.backend.BackendApplication
import blue.mild.breviary.backend.dtos.UserDtoOut
import blue.mild.breviary.backend.dtos.UserSignupDtoIn
import blue.mild.breviary.backend.services.AuthenticationService
import blue.mild.breviary.backend.services.JWTAuthenticationFilter
import mu.KLogging
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import pw.forst.tools.katlib.parseJson
import java.net.URI
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest(
    classes = [BackendApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
class ControllerTest {

    companion object : KLogging() {
        fun getAuthHeaders(): HttpHeaders =
            JWTAuthenticationFilter.createHeadersWithJsonWebToken(TEST_USERNAME, "Cookie")
    }

    @Autowired
    lateinit var client: TestRestTemplate

    @Autowired
    lateinit var authenticationService: AuthenticationService

    @Autowired
    lateinit var flyway: Flyway

    @BeforeEach
    fun init() {
        flyway.clean()
        flyway.migrate()
    }

    @Value(value = ApiRoutes.BASE_PATH)
    val apiPrefix: String? = null

    @LocalServerPort
    var port: Int = 0

    fun <T> executeClient(
        httpMethod: HttpMethod,
        endpoint: String,
        expectedHttpStatuses: Set<HttpStatus>,
        cls: ParameterizedTypeReference<T>,
        requestMessage: HttpEntity<Any>? = null
    ): ResponseEntity<T> {
        val result = client.exchange(
            URI("http://localhost:$port$endpoint"),
            httpMethod,
            requestMessage ?: HttpEntity.EMPTY,
            cls
        )

        logger.info {
            "Received data: ${result.body}."
        }
        logger.info {
            "Expected statuses: ${expectedHttpStatuses.joinToString()} and received ${result.statusCode}."
        }
        assertTrue(
            expectedHttpStatuses.contains(result.statusCode),
            "Expected statuses: ${expectedHttpStatuses.joinToString()}, but received ${result.statusCode}."
        )
        return result
    }

    fun <T> executeClientGet(
        endpoint: String,
        expectedHttpStatuses: Set<HttpStatus>,
        cls: ParameterizedTypeReference<T>,
        headers: HttpHeaders = HttpHeaders()
    ): ResponseEntity<T> = executeClient(HttpMethod.GET, endpoint, expectedHttpStatuses, cls, HttpEntity<Any>(headers))

    fun <T> executeClientPost(
        endpoint: String,
        expectedHttpStatuses: Set<HttpStatus>,
        cls: ParameterizedTypeReference<T>,
        requestMessage: HttpEntity<Any>? = null
    ): ResponseEntity<T> = executeClient(HttpMethod.POST, endpoint, expectedHttpStatuses, cls, requestMessage)

    fun <T> executeClientPut(
        endpoint: String,
        expectedHttpStatuses: Set<HttpStatus>,
        cls: ParameterizedTypeReference<T>,
        requestMessage: HttpEntity<Any>? = null
    ): ResponseEntity<T> = executeClient(HttpMethod.PUT, endpoint, expectedHttpStatuses, cls, requestMessage)

    fun <T> executeClientDelete(
        endpoint: String,
        expectedHttpStatuses: Set<HttpStatus>,
        cls: ParameterizedTypeReference<T>,
        headers: HttpHeaders = HttpHeaders()
    ): ResponseEntity<T> =
        executeClient(HttpMethod.DELETE, endpoint, expectedHttpStatuses, cls, HttpEntity<Any>(headers))

    fun <T> testExpectedCountOfItems(
        endpoint: String,
        expectedCount: Int
    ): String {
        val result = executeClientGet(
            endpoint,
            setOf(HttpStatus.OK),
            typeReference<String>()
        )
        val body = result.body
        assertNotNull(body)
        val items = parseJson<List<T>>(body)
        assertEquals(expectedCount, items?.size)
        return body
    }

    fun signup() = executeClientPost(
        "$apiPrefix/signup",
        setOf(HttpStatus.CREATED),
        typeReference<UserDtoOut>(),
        HttpEntity<Any>(
            UserSignupDtoIn(
                email = TEST_USERNAME,
                password = TEST_PASSWORD,
                firstName = TEST_FIRST_NAME,
                lastName = TEST_LAST_NAME
            )
        )
    )

    fun authenticateUser() {
        SecurityContextHolder.getContext().authentication =
            authenticationService.createdAuthenticationToken(TEST_USERNAME)
    }
}

/**
 * Helper inline type ref function.
 *
 * @param T
 */
inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}
