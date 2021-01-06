package blue.mild.breviary.backend.services

import blue.mild.breviary.backend.config.WebSecurityConfig
import blue.mild.breviary.backend.config.WebSecurityConfig.Companion.SECRET
import blue.mild.breviary.backend.config.WebSecurityConfig.Companion.SET_COOKIE_KEY
import blue.mild.breviary.backend.config.WebSecurityConfig.Companion.createCookie
import com.fasterxml.jackson.module.kotlin.readValue
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import pw.forst.tools.katlib.jacksonMapper
import java.util.Date
import java.util.UUID
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * JWTAuthenticationFilter.
 *
 * @property authManager
 */
class JWTAuthenticationFilter(
    private val authManager: AuthenticationManager,
    private val authenticationService: AuthenticationService
) : UsernamePasswordAuthenticationFilter() {

    companion object {
        private const val RANDOM_VALUE_HEADER = "random_value"

        /**
         * Function to create JWT token.
         *
         * @param username
         * @param expiration
         * @return
         */
        fun createJsonWebToken(username: String, expiration: Date): String =
            Jwts.builder()
                .setSubject(username)
                .setExpiration(expiration)
                .setHeaderParam(RANDOM_VALUE_HEADER, UUID.randomUUID())
                .signWith(SignatureAlgorithm.HS512, SECRET.toByteArray())
                .compact()

        /**
         * Creates HTTP headers with JWT.
         *
         * @param username
         * @return
         */
        fun createHeadersWithJsonWebToken(username: String, cookieKey: String): HttpHeaders =
            createJsonWebToken(username, getCookieDuration()).let {
                HttpHeaders().apply { add(cookieKey, createCookie(it)) }
            }

        /**
         * Gets cookie duration.
         *
         */
        fun getCookieDuration(): Date =
            Date(System.currentTimeMillis() + WebSecurityConfig.EXPIRATION_TIME * WebSecurityConfig.MILLIS)
    }

    /**
     * UserCredentials.
     *
     * @property username
     * @property password
     */
    data class UserCredentials(
        val username: String,
        val password: String
    )

    @Suppress("TooGenericExceptionCaught")
    override fun attemptAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse
    ): Authentication {
        try {
            val credentials = jacksonMapper().readValue<UserCredentials>(req.inputStream)
            return authManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    credentials.username,
                    credentials.password,
                    authenticationService.getAuthorities(credentials.username)
                )
            )
        } catch (e: Exception) {
            throw AuthenticationCredentialsNotFoundException(e.message)
        }
    }

    override fun successfulAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse,
        chain: FilterChain,
        auth: Authentication
    ) {
        createJsonWebToken(
            (auth.principal as User).username,
            getCookieDuration()
        ).also { res.setHeader(SET_COOKIE_KEY, createCookie(it)) }
    }
}
