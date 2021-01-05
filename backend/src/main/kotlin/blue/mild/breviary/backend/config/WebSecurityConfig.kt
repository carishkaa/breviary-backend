package blue.mild.breviary.backend.config

import blue.mild.breviary.backend.ApiRoutes
import blue.mild.breviary.backend.dtos.ResponseDto
import blue.mild.breviary.backend.services.AuthenticationService
import blue.mild.breviary.backend.services.JWTAuthenticationFilter
import blue.mild.breviary.backend.services.JWTAuthorizationFilter
import org.apache.tomcat.util.http.Rfc6265CookieProcessor
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import pw.forst.tools.katlib.createJson
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * WebSecurityConfig.
 *
 * @property userDetailsService
 * @property bCryptPasswordEncoder
 */
@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    @Qualifier("DBUserDetails")
    private val userDetailsService: UserDetailsService,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val authenticationService: AuthenticationService
) : WebSecurityConfigurerAdapter() {

    companion object {
        const val AUTH_COOKIE_NAME = "Authorization"
        const val EXPIRATION_TIME = 3600 * 24 * 7 // 7 days in seconds
        const val MILLIS = 1000
        const val SECRET = "super-secret-lopata-123"
        const val SET_COOKIE_KEY = "Set-Cookie"

        /**
         * Helper function to create Cookie string.
         *
         * @param value
         * @param expiration
         * @return
         */
        fun createCookie(value: String, expiration: Int = EXPIRATION_TIME): String {
            val cookie = Cookie(AUTH_COOKIE_NAME, value)
                .apply {
                    secure = true
                    isHttpOnly = true
                    maxAge = expiration
                }

            @Suppress("DEPRECATION") // TODO fix me
            return Rfc6265CookieProcessor()
                .apply { setSameSiteCookies("None") }
                .generateHeader(cookie)
        }
    }

    /**
     * CustomLogoutHandler.
     *
     */
    inner class CustomLogoutHandler : LogoutHandler {
        override fun logout(
            request: HttpServletRequest,
            response: HttpServletResponse,
            authentication: Authentication?
        ) {
            with(response) {
                setHeader(SET_COOKIE_KEY, createCookie("", 0))
                contentType = MediaType.APPLICATION_JSON.toString()
                writer.let {
                    it.println(createJson(ResponseDto("You have been logged out.", HttpStatus.OK.value())))
                    it.flush()
                }
            }
        }
    }

    /**
     * Configuration.
     *
     * @param http
     */
    override fun configure(http: HttpSecurity) {
        http
            .cors()
            .and()
            .csrf().disable()
            .exceptionHandling().authenticationEntryPoint { _, response, authException ->
                response.status = HttpStatus.UNAUTHORIZED.value()
                response.contentType = MediaType.APPLICATION_JSON.toString()
                response.writer.let {
                    it.println(
                        createJson(
                            ResponseDto(
                                authException.message ?: "Could not authenticate.",
                                response.status
                            )
                        )
                    )
                    it.flush()
                }
            }
            .and()
            .authorizeRequests()
            .antMatchers(
                "/api/${ApiRoutes.SIGNUP}",
                "/api/${ApiRoutes.LOGIN}",
                "/api/${ApiRoutes.VERSION}",
                "/api/${ApiRoutes.PasswordReset.BASE}",
                "/api/${ApiRoutes.PasswordReset.BASE}/${ApiRoutes.PasswordReset.REQUEST}",
                "/actuator**",
                "/actuator/health**",
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/v3/api-docs",
                "/webjars/**"
            ).permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilter(JWTAuthenticationFilter(authenticationManager(), authenticationService))
            .addFilter(JWTAuthorizationFilter(authenticationManager(), authenticationService))
            .logout()
            .addLogoutHandler(CustomLogoutHandler())
            .logoutRequestMatcher(AntPathRequestMatcher("/api/${ApiRoutes.LOGOUT}", "POST"))
            .logoutSuccessHandler { _, _, _ -> }
            .permitAll()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    /**
     * Configuration.
     *
     * @param auth
     */
    public override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder)
    }
}
