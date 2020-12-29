package blue.mild.breviary.backend.services

import blue.mild.breviary.backend.config.WebSecurityConfig.Companion.AUTH_COOKIE_NAME
import blue.mild.breviary.backend.config.WebSecurityConfig.Companion.SECRET
import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * JWTAuthorizationFilter.
 *
 * @param authManager
 */
class JWTAuthorizationFilter(
    authManager: AuthenticationManager,
    private val authenticationService: AuthenticationService
) : BasicAuthenticationFilter(authManager) {

    override fun doFilterInternal(
        req: HttpServletRequest,
        res: HttpServletResponse,
        chain: FilterChain
    ) {
        // set cookie in session context if exists and it is not blank
        req.cookies
            ?.find { it.name == AUTH_COOKIE_NAME }
            ?.takeIf { it.value.isNotBlank() }
            ?.also { SecurityContextHolder.getContext().authentication = getAuthentication(it) }

        // resume chain
        chain.doFilter(req, res)
    }

    private fun getAuthentication(cookie: Cookie): UsernamePasswordAuthenticationToken? =
        Jwts.parser()
            .setSigningKey(SECRET.toByteArray())
            .parseClaimsJws(cookie.value)
            .body.subject
            ?.let { authenticationService.createdAuthenticationToken(it) }
}
