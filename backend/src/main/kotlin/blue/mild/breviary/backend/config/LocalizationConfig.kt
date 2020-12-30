package blue.mild.breviary.backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.Locale

/**
 * Localization configuration.
 *
 */
@Configuration
class LocalizationConfig : WebMvcConfigurer {

    companion object {
        const val HEADER_NAME: String = "lang"
    }

    /**
     * LocaleResolver bean.
     *
     * @return
     */
    @Bean
    fun localeResolver(): LocaleResolver =
        SessionLocaleResolver().apply { setDefaultLocale(Locale.US) }

    /**
     * LocaleChangeInterceptor bean.
     *
     * @return
     */
    @Bean
    fun localeChangeInterceptor(): LocaleChangeInterceptor =
        LocaleChangeInterceptor().apply { paramName = HEADER_NAME }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(localeChangeInterceptor())
    }
}
