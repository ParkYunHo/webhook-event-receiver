package com.john.webhook.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.john.webhook.common.utils.GithubUtils
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration

/**
 * @author yoonho
 * @since 2022.12.25
 */
@Configuration
class AppConfig(
    private val objectMapper: ObjectMapper
) {

    @PostConstruct
    fun init() {
        GithubUtils.init(objectMapper)
    }
}