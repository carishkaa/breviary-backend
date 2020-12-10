package blue.mild.breviary.backend.services

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service

/**
 * Helper wrapper to get localization messages.
 *
 * @property messageSource
 */
@Service
class LocaleService(
    private val messageSource: MessageSource
) {
    /**
     * Gets localization message by its code.
     *
     * @param code
     * @param args
     * @return
     */
    fun getMessage(code: String, args: Array<Any>? = null): String =
        messageSource.getMessage(code, args, LocaleContextHolder.getLocale())
}
