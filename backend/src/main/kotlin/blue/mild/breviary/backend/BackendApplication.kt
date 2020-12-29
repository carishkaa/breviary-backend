package blue.mild.breviary.backend

import org.apache.tomcat.util.http.Rfc6265CookieProcessor
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer
import org.springframework.boot.web.servlet.ServletContextInitializer
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.servlet.ServletContext

@EnableScheduling
@SpringBootApplication(scanBasePackages = [App.SPRING_BOOT_APP_BASE_PACKAGES])
@EntityScan(basePackages = [App.ENTITY_SCAN_BASE_PACKAGES])
@EnableJpaRepositories(basePackages = [App.JPA_REPOSITORIES_SCAN_BASE_PACKAGES])
@EnableGlobalMethodSecurity(prePostEnabled = true)
/**
 * BackendApplication.
 */
data class BackendApplication(
    private val env: Environment,
    @Value("\${spring.application.name}")
    private val appName: String
) : ServletContextInitializer {

    override fun onStartup(servletContext: ServletContext) {
        servletContext.sessionCookieConfig.name = "JSESSIONID-$appName"
        servletContext.sessionCookieConfig.isSecure = true
    }

    /**
     * TomcatContextCustomizer bean.
     *
     * @return
     */
    @Bean
    fun sameSiteCookieTomcatContextCustomizer(): TomcatContextCustomizer = TomcatContextCustomizer { context ->
        val cookieProcessor = Rfc6265CookieProcessor()
        cookieProcessor.setSameSiteCookies("None")
        context?.cookieProcessor = cookieProcessor
    }

    /**
     * Password encoder.
     *
     * @return
     */
    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder? = BCryptPasswordEncoder()
}

/**
 * Main function.
 *
 * @param args
 */
fun main(args: Array<String>) {
    @Suppress("SpreadOperator")
    SpringApplication.run(BackendApplication::class.java, *args)
}
