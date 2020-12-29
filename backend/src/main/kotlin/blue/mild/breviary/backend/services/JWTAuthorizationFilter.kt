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
        if (req.cookies == null) {
            chain.doFilter(req, res)
            return
        }
        val cookie = req.cookies.find { it.name == AUTH_COOKIE_NAME }

        if (cookie == null || cookie.value.isBlank()) {
            chain.doFilter(req, res)
            return
        }

        val authentication = getAuthentication(cookie)
        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(req, res)
    }

    private fun getAuthentication(cookie: Cookie): UsernamePasswordAuthenticationToken? {
        val user = Jwts.parser()
            .setSigningKey(SECRET.toByteArray())
            .parseClaimsJws(cookie.value)
            .body
            .subject

        return if (user != null) {
            authenticationService.createdAuthenticationToken(user)
        } else null
    }
}
