package blue.mild.breviary.backend.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * WebMvcConfig.
 *
 */
@Configuration
class WebMvcConfig(
    @Value("\${spring.application.allowedOrigins}")
    private val allowedOrigins: List<String>
) : WebMvcConfigurer {

    @Suppress("SpreadOperator")
    override fun addCorsMappings(registry: CorsRegistry) {
        registry
            .addMapping("/**")
            .allowedOrigins(*allowedOrigins.toTypedArray())
            .allowCredentials(true)
            .allowedMethods("*")
    }
}
