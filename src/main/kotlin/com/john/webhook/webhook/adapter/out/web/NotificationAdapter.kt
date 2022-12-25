package com.john.webhook.webhook.adapter.out.web

import com.john.webhook.common.constants.CommCode
import com.john.webhook.common.exception.BadRequestException
import com.john.webhook.common.exception.InternalServerErrorException
import com.john.webhook.webhook.application.port.out.NotificationPort
import io.netty.handler.codec.json.JsonObjectDecoder
import jakarta.annotation.PostConstruct
import org.apache.tomcat.util.json.JSONParser
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono

/**
 * @author yoonho
 * @since 2022.12.25
 */
@Component
class NotificationAdapter(
    private val webClientBuilder: WebClient.Builder
): NotificationPort {

    private val log = LoggerFactory.getLogger(this::class.java)
    private lateinit var webClient: WebClient

    @Value("\${webhook.apis.slack}")
    private lateinit var slackUrl: String

    @PostConstruct
    fun initWebClient() {
        this.webClient = webClientBuilder.build()
    }

    override fun notify(channel: String, message: String) {
        when(channel) {
            CommCode.ChannelType.SLACK.code -> this.slack(message)
        }
    }

    private fun slack(message: String) {
        try{
            val uriComponents = UriComponentsBuilder
                .fromHttpUrl(slackUrl)
                .build(false)

            val bodyMap = mapOf(
                "username" to "Github Notification",
                "text" to message
            )

            webClient.post()
                .uri(uriComponents.toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(bodyMap))
                .exchangeToMono { response ->
                     if(response.statusCode().is4xxClientError) {
                         throw BadRequestException("알림을 전송하지 못했습니다.")
                    }else if(response.statusCode().is5xxServerError) {
                        throw InternalServerErrorException()
                    }else{
                         return@exchangeToMono response.toBodilessEntity()
                     }
                }
                .block()

        }catch (be: BadRequestException) {
            log.info("BadRequestException - message: {}", be.message)
            throw be
        }catch(e: Exception){
            log.info("Exception - message: {}", e.message)
            throw e
        }
    }
}