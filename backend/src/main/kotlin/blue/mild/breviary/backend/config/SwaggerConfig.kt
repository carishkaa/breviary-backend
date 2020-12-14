package blue.mild.breviary.backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import springfox.documentation.builders.PathSelectors.regex
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.schema.ModelRef
import springfox.documentation.service.ApiDescription
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.service.Operation
import springfox.documentation.service.Response
import springfox.documentation.service.ResponseMessage
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.DocumentationType.OAS_30
import springfox.documentation.spi.service.ApiListingBuilderPlugin
import springfox.documentation.spi.service.contexts.ApiListingContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.common.SwaggerPluginSupport
import java.util.Optional

/**
 * SwaggerConfig.
 *
 */
@Configuration
class SwaggerConfig {
    /**
     * Docket.
     *
     * @return
     */
    @Bean
    fun docket(): Docket {
        return Docket(OAS_30)
            .genericModelSubstitutes(ResponseEntity::class.java)
            .forCodeGeneration(true)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(
                regex("/api/.*")
                    .or(
                        regex("/actuator/")
                            .or(
                                regex("/actuator/health")
                            )
                    )
            )
            .build()
            .apiInfo(apiInfo())
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfo(
            "Breviary REST API Specification",
            "Breviary REST API Specification.",
            "v1.0.0",
            "https://mild.blue",
            Contact("Breviary", "https://breviary.mild.blue", "breviary@mild.blue"),
            "Mild Blue",
            "https://mild.blue",
            listOf()
        )
    }
}

/**
 * Swagger config plugin to be able to define custom operations into Swagger spec.
 *
 */
@Suppress("LongMethod")
@Component
@Order(value = SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
class LogoutApiListing : ApiListingBuilderPlugin {
    /**
     * Applies custom schema changes.
     *
     * @param apiListingContext
     */
    override fun apply(apiListingContext: ApiListingContext?) {
        // This reflection hell must be done to be able to just append generated operations, not rewrite them.
        val builder = apiListingContext!!.apiListingBuilder()
        val field = org.springframework.util.ReflectionUtils.findField(builder.javaClass, "apis")!!
        org.springframework.util.ReflectionUtils.makeAccessible(field)
        val value = Optional.ofNullable(org.springframework.util.ReflectionUtils.getField(field, builder))
            .orElse(emptyList<ApiDescription>())

        @Suppress("UNCHECKED_CAST")
        val apis = value as List<ApiDescription>
        val newApis = mutableListOf<ApiDescription>()
        newApis.addAll(apis)
        newApis.add(
            ApiDescription(
                "/api/logout",
                "/api/logout",
                "Logout Controller",
                "Logout Controller",
                listOf(
                    Operation(
                        HttpMethod.POST,
                        "Logout user from the application.",
                        "Logout Controller",
                        null,
                        null,
                        "logoutUsingPOST",
                        0,
                        hashSetOf("logout-controller"),
                        hashSetOf(),
                        hashSetOf(),
                        hashSetOf(),
                        listOf(),
                        listOf(),
                        hashSetOf(
                            ResponseMessage(
                                HttpStatus.OK.value(),
                                HttpStatus.OK.reasonPhrase,
                                ModelRef("String"),
                                listOf(),
                                mapOf(),
                                listOf()
                            ),
                            ResponseMessage(
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.reasonPhrase,
                                null,
                                listOf(),
                                mapOf(),
                                listOf()
                            ),
                            ResponseMessage(
                                HttpStatus.UNAUTHORIZED.value(),
                                HttpStatus.UNAUTHORIZED.reasonPhrase,
                                null,
                                listOf(),
                                mapOf(),
                                listOf()
                            ),
                            ResponseMessage(
                                HttpStatus.FORBIDDEN.value(),
                                HttpStatus.FORBIDDEN.reasonPhrase,
                                null,
                                listOf(),
                                mapOf(),
                                listOf()
                            ),
                            ResponseMessage(
                                HttpStatus.NOT_FOUND.value(),
                                HttpStatus.NOT_FOUND.reasonPhrase,
                                null,
                                listOf(),
                                mapOf(),
                                listOf()
                            )
                        ),
                        "false",
                        false,
                        listOf(),
                        setOf(),
                        null,
                        setOf(
                            Response(
                                HttpStatus.OK.value().toString(),
                                HttpStatus.OK.reasonPhrase,
                                true,
                                setOf(),
                                listOf(),
                                listOf(),
                                listOf()
                            ),
                            Response(
                                HttpStatus.BAD_REQUEST.value().toString(),
                                HttpStatus.BAD_REQUEST.reasonPhrase,
                                false,
                                setOf(),
                                listOf(),
                                listOf(),
                                listOf()
                            ),
                            Response(
                                HttpStatus.UNAUTHORIZED.value().toString(),
                                HttpStatus.UNAUTHORIZED.reasonPhrase,
                                false,
                                setOf(),
                                listOf(),
                                listOf(),
                                listOf()
                            ),
                            Response(
                                HttpStatus.FORBIDDEN.value().toString(),
                                HttpStatus.FORBIDDEN.reasonPhrase,
                                false,
                                setOf(),
                                listOf(),
                                listOf(),
                                listOf()
                            ),
                            Response(
                                HttpStatus.NOT_FOUND.value().toString(),
                                HttpStatus.NOT_FOUND.reasonPhrase,
                                false,
                                setOf(),
                                listOf(),
                                listOf(),
                                listOf()
                            )
                        )
                    )
                ),
                false
            )
        )
        builder.apis(newApis)
    }

    /**
     * @param delimiter
     * @return
     */
    override fun supports(delimiter: DocumentationType): Boolean = true
}
